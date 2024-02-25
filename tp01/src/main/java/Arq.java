
//metodos de escrita e leitura aqui 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;

public class Arq {
    /**
     * InnerArq
     */

     // procura o registro no arquivo
    protected static class DataFinder {
        int ID;
        long seekSaver;
        long metaSeek;
        MetaData metaData;

        public boolean FindSong(int ID) { // procura a musica por id
            IniciarLeituraSequencial(); // funçao que le o ultimo id inserido

            int idSeacher = -1;
            try {
                for (; idSeacher != ID || meta.lapide; raf.seek(raf.getFilePointer() + (meta.sizeBytes - 4))) { // pula o numero de bytes ate achar o registro
            
                    this.metaSeek = raf.getFilePointer(); // salva a posição do inicio da lapide
                    meta.readMetaData(); // le os metadados
                    this.metaData = meta; // salva os metadados na classe
                    this.seekSaver = raf.getFilePointer(); // salva a posição do inicio do registro
                    idSeacher = raf.readInt(); // le o ID
                }
                return true;
            } catch (IOException e) {
                System.out.println("Erro ao buscar a música: " + e.getMessage());
                return false;
            }

        }

    }

    protected static boolean status = false; // se esta em operação
    protected static String file; // nome do arquivo
    protected static RandomAccessFile raf;
    protected static DataOutputStream dos;
    protected static DataInputStream dis;
    protected static MetaData meta;

    // inicia o arquivo/cria o raf para gerenciar
    public static void Iniciar(String filex) {
        file = filex;
        try {
            raf = new RandomAccessFile(file, "rw"); // cria pra escrita/leitura
            status = true;
            meta = new MetaData(raf);
            Musica.setLastID(raf.readInt());  // pega o ultimo id inserido
        } catch (IOException e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
        }

    }

    // procurar a musica 
    public static Musica FindSongID(int ID) {
        Musica musica = null;
        DataFinder finder = new DataFinder();
        if (finder.FindSong(ID)) { // se ele achar a musica
            try{
                raf.seek(finder.metaSeek); // coloca o cabeçote no inicio do registro
                musica = getRegistro(); // le o registro e cria o objeto
            }catch(IOException e){
                System.out.println("Erro ao buscar a música: " + e.getMessage());
            }
        
        } 
        return musica;
    }

    // pega o status do programa
    public static boolean getStatus() {
        return status;
    }

    // deleta o som pelo id
    public static boolean DeleteSong(int id) {
        boolean status = false;
        DataFinder finder = new DataFinder();
        if (finder.FindSong(id)) { //encontrou a musica a ser deletada
            try {
                raf.seek(finder.metaSeek); // coloca o cabeçote no inicio do registro que vai ser deletado
                raf.writeBoolean(true); // seta a lapide como true (que deus o tenha)
                status = true;
            } catch (IOException e) {
                System.out.println("Erro ao deletar a música: " + e.getMessage());
            }
        } else { // se nao encontrou a musica
            System.out.println("Musica não encontrada");
        }
        return status;
    }

    // atualiza os dados da musica
    public static void UpdateSong(int ID, String newSong) {
        DataFinder finder = new DataFinder();
        if(finder.FindSong(ID)){
           try{
            Musica nova = Musica.StringToMusica(newSong); // cria um novo objeto com a string de parametro
            int novaSize = nova.toByteArray().length; // pega o tamanho da string em bytes

            if (novaSize <= finder.metaData.sizeBytes) { // pode sobrescrever (mesmo tamanho ou igual)
                raf.seek(finder.seekSaver); // coloca o ponteiro no inicio do registro
                raf.write(nova.toByteArray()); // sobrescreve
                // volta para o inicio do registro (lapide)
            } else { // se não puder sobrescrever (for maior)
                raf.seek(finder.metaSeek); // volta para o inicio do registro (lapide)
                raf.writeBoolean(true); // marca como lapide
                addRegistroExistenteEOF(nova);// update em fim de arquivo sem alterar ultimo ID
            }
        }catch(IOException e){
            System.out.println("Erro ao atualizar a música: " + e.getMessage());
        }
       
        }else{
            System.out.println("Musica não encontrada");
        }
       
    }

    public static void IniciarLeituraSequencial() {
        try {
            raf.seek(4);// pula o lastID
        } catch (IOException e) {
            System.out.println("Erro ao iniciar a leitura sequencial: " + e.getMessage());
        }
    }

    // le o registro
    public static Musica getRegistro() {
        try {
            Musica buffer = new Musica();
            byte[] bytes;
            do { 
                meta.readMetaData();
                bytes = new byte[meta.sizeBytes]; // Cria um array de bytes com o tamanho do registro
                raf.readFully(bytes); // Lê o registro completo
            } while (meta.lapide && raf.getFilePointer() < raf.length()); //so retorna reg valido

            buffer = Musica.fromByteArray(bytes); 
            return buffer;
        } catch (IOException e) {

            System.out.println("Arquivo chegou ao fim" + e.getMessage());
            return null;
        }

    }


    // Adiciona um novo registro
    public static boolean addRegistro(String str) {
        byte[] ba;
        Musica nova = Musica.StringToMusica(str); // cria o objeto com a string
        nova.id = Musica.getLastID() + 1; // atualiza o ultimo id
        try {
            ba = nova.toByteArray(); // atualiza lastID na classe mas não no arquivo
            Musica.setLastID(nova.getId()); // atualiza na classe
            writeLastID(Musica.getLastID()); // atualiza no arquivo
            raf.seek(raf.length()); // joga o ponteiro do RAF para o final do arquivo
            meta.writeMetaData(ba.length); // escreve os metadados do registro
            raf.write(ba); // escreve o registro
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

// adiciona um registro existente
    public static boolean addRegistroExistenteEOF(Musica newsong) {
        try {
            byte[] ba = newsong.toByteArray(); // tranforma o registro em array de bytes
            raf.seek(raf.length()); // leva o ponteiro do RAF para o final do arquivo
            meta.writeMetaData(ba.length); // escreve o metadado do registro
            raf.write(ba); // escreve o registro
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // le o ultimo id inserido 
    public static int ReadLastID() {
        try {
            raf.seek(0); // vai pro inicio do arquivo
            return raf.readInt(); // le o id
        } catch (IOException e) {
            System.out.println("Erro ao ler o último ID: " + e.getMessage());
            return -1;
        }

    }

    // escreve o ultimo id inserido
    public static void writeLastID(int id) {
        try {
            raf.seek(0); // vai pro inicio do arquivo
            raf.writeInt(id); // escreve o ultimo id
        } catch (IOException e) {
            System.out.println("Erro ao escrever o último ID: " + e.getMessage());
        }
    }

    // finaliza o programa
    public static void Finalizar() {
        status = false;
        try {
            raf.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
        }

    }

}
