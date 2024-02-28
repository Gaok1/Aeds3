
//metodos de escrita e leitura aqui 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;

public class Arq {

    private static class DataFinder {
        long seekSaver;
        long metaSeek;
        MetaData metaData; // irá armazenar os metadados da musica Encontrada
        Musica musica; // irá armazenar a musica encontrada

        public boolean FindSong(int ID) { // procura a musica por id
            IniciarLeituraSequencial(); // funçao que le o ultimo id inserido

            int idSeacher = -1;
            try {
                Logs.Details("Procurando a música com ID: " + ID + "...");
                for (; idSeacher != ID || Arq.meta.lapide; raf.seek(raf.getFilePointer() + (Arq.meta.sizeBytes - 4))) { // pula
                    // o
                    // numero
                    // de
                    // bytes
                    // ate
                    // achar
                    // o
                    // registro
                    this.metaSeek = raf.getFilePointer(); // salva a posição do inicio da lapide
                    Arq.meta.readMetaData(); // le os metadados
                    this.metaData = Arq.meta; // salva os metadados na classe
                    this.seekSaver = raf.getFilePointer(); // salva a posição do inicio do registro
                    idSeacher = raf.readInt(); // le o ID
                }
                Logs.Succeed("Musica encontrada: " + idSeacher);
                raf.seek(this.metaSeek); // volta para o inicio do registro
                this.musica = Arq.getRegistro(); // le o registro e cria o objeto

                return true;
            } catch (IOException e) {
                Logs.Alert("Erro ao buscar a música: " + e.getMessage());
                this.metaData = null;
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
        Arq.file = "Source/DataBase/songs.db";
        try {
            Arq.raf = new RandomAccessFile(file, "rw"); // cria pra escrita/leitura
            Arq.status = true;
            Arq.meta = new MetaData(raf);
            Musica.setLastID(raf.readInt()); // pega o ultimo id inserido
            Logs.Succeed("Arquivo aberto com sucesso!\nClasse Arq Iniciada com sucesso!");
        } catch (IOException e) {
            Logs.Alert("Erro ao abrir o arquivo: " + e.getMessage());
        }

    }

    // procurar a musica
    public static Musica FindSongID(int ID) {
        Musica musica = null;
        DataFinder finder = new DataFinder();
        if (finder.FindSong(ID)) { // se ele achar a musica
            musica = finder.musica; // retorna a musica
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
        if (finder.FindSong(id)) { // encontrou a musica a ser deletada
            try {
                raf.seek(finder.metaSeek); // coloca o cabeçote no inicio do registro que vai ser deletado
                raf.writeBoolean(true); // seta a lapide como true (que deus o tenha)
                status = true;
                Logs.Succeed("Musica deletada com sucesso!");
            } catch (IOException e) {
                Logs.Alert("Erro ao deletar a música: " + e.getMessage());
            }
        } else { // se nao encontrou a musica
            Logs.Alert("Musica não encontrada");
        }
        return status;
    }

    // atualiza os dados da musica
    public static void UpdateSong(int ID, String newSong) {
        DataFinder finder = new DataFinder();
        if (finder.FindSong(ID)) {
            try {
                Musica nova = Musica.StringToMusica(newSong); // cria um novo objeto com a string de parametro
                int novaSize = nova.toByteArray().length; // pega o tamanho da string em bytes

                if (novaSize <= finder.metaData.sizeBytes) { // pode sobrescrever (mesmo tamanho ou igual)
                    raf.seek(finder.seekSaver); // coloca o ponteiro no inicio do registro
                    raf.write(nova.toByteArray()); // sobrescreve
                    // volta para o inicio do registro (lapide)
                    Logs.Details("Registro Alterado é menor ou igual ao anterior, sobrescrevendo...");
                } else { // se não puder sobrescrever (for maior)
                    raf.seek(finder.metaSeek); // volta para o inicio do registro (lapide)
                    raf.writeBoolean(true); // marca como lapide
                    addRegistroExistenteEOF(nova);// update em fim de arquivo sem alterar ultimo ID
                    Logs.Details(
                            "Registro Alterado é maior que o anterior, deletando e adicionando no fim do arquivo...");
                }
                Logs.Succeed("Musica atualizada com sucesso!");
            } catch (IOException e) {
                Logs.Alert("Erro no update : " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Logs.Alert("Erro no Update :\n Illegal Argument Exception" + e.getMessage());
            } catch (NullPointerException e) {
                Logs.Alert("Erro no Update :\n Null Pointer Exception" + e.getMessage());
            }

        } else {
            Logs.Alert("Musica não encontrada");
        }
    }

    public static void IniciarLeituraSequencial() {

        try {
            raf.seek(4);// pula o lastID
            Logs.Details("Leitura sequencial iniciada!");
        } catch (IOException e) {
            Logs.Alert("Erro ao iniciar a leitura sequencial: " + e.getMessage());
        }
    }

    // le o registro
    public static Musica getRegistro() {
        Musica buffer = new Musica();
        try {

            byte[] bytes;
            do {
                Arq.meta.readMetaData();
                bytes = new byte[Arq.meta.sizeBytes]; // Cria um array de bytes com o tamanho do registro
                raf.readFully(bytes); // Lê o registro completo
            } while (Arq.meta.lapide); // so retorna reg valido

            buffer = Musica.fromByteArray(bytes);
            return buffer;
        } catch (IOException e) {
            if (e instanceof java.io.EOFException) {
                Logs.Succeed("Leitura Finalizada!\n Raf esta em EOF Exception :" + e.getMessage());
                return null;
            }
            Logs.Alert("Erro em Leitura de Registro \nException :" + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            Logs.Alert("Erro na Conversão do Registro : " + e.getMessage());
            Logs.KindaAlert("Registro Lido: " + buffer.toString());
            Logs.KindaAlert("Lapide: " + Arq.meta.lapide + " Tamanho: " + Arq.meta.sizeBytes);
            return null;
        }

    }

    // Adiciona um novo registro
    public static boolean addRegistro(String str) {
        byte[] ba;
        try {
            Musica nova = Musica.StringToMusica(str); // cria o objeto com a string
            nova.id = Musica.getLastID() + 1; // seta o id da nova musica

            ba = nova.toByteArray(); // atualiza lastID na classe mas não no arquivo
            Musica.setLastID(nova.getId()); // atualiza na classe
            writeLastID(Musica.getLastID()); // atualiza no arquivo
            raf.seek(raf.length()); // joga o ponteiro do RAF para o final do arquivo
            meta.writeMetaData(ba.length); // escreve os metadados do registro
            raf.write(ba); // escreve o registro
            Logs.Succeed("Registro Adicionado com sucesso!");
            return true;
        } catch (IOException e) {
            Logs.Alert(" Erro em Adicionar Registro addRegistro()\n error = " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            Logs.Alert("Erro na Conversão da String : " + e.getMessage());
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
            Logs.Succeed("Registro Realocado com Sucesso!");
            return true;
        } catch (IOException e) {
            Logs.Alert("Erro ao Adicionar registros em fim de arquivo!\n addRegistroExistenteEOF Exception :"
                    + e.getMessage());
            return false;
        }
    }

    // le o ultimo id inserido
    public static int ReadLastID() {
        try {
            raf.seek(0); // vai pro inicio do arquivo
            return raf.readInt(); // le o id
        } catch (IOException e) {
            Logs.Alert("Erro ao ler o último ID: " + e.getMessage());
            return -1;
        }

    }

    // escreve o ultimo id inserido
    public static void writeLastID(int id) {
        try {
            raf.seek(0); // vai pro inicio do arquivo
            raf.writeInt(id); // escreve o ultimo id
            Logs.Succeed("Ultimo ID escrito com sucesso!");
            Logs.Details("Ultimo ID: " + id);
        } catch (IOException e) {
            Logs.Alert("Erro ao escrever o último ID: " + e.getMessage());
        }
    }

    // finaliza o programa
    public static void Finalizar() {
        status = false;
        try {
            raf.close(); // fecha o arquivo
        } catch (IOException e) {
            Logs.Alert("Erro ao fechar o arquivo: " + e.getMessage());
        }

    }

}
