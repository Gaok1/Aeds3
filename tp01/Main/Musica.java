

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Musica {
    private static int LastID = 0;
    int id; //4 
    String nome;  
    String artista; 
    int popular;//4
    long date;//8
    String []genero; //vairos generos (2)
    float dance;
    String hash;

    //getter
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getArtista() {
        return artista;
    }
    public int getPopularidade() {
        return popular;
    }
    public String getDataLancamento() {
        return timestampConv(date);
    }
    public String getGenero() {
        return genero[0] + ", " + genero[1];
    }
    public float getDancabilidade() {
        return dance;
    }
    public String getHash() {
        return hash;
    }
    
    public Musica() {
        this.nome = "Vazio";
        this.artista = "Nenhum"; //nome do artista limitado
        this.popular = 0;
        this.date = 0;
        this.dance = 0;
        this.genero = iniciarGenero();
        this.hash = "";
    }

 



    public Musica(String nome, String artista, int popular, String date, String genero, float dance, String hash) {
        this.nome = nome;
        this.artista = artista;
        this.popular = popular;
        try {
            this.date = dataConverter(date);
        } catch (Exception e) {
            Logs.Alert("Erro ao converter a data: " + e.getMessage());
        }
        this.genero = genero.split(";");
        this.dance = dance;
        this.hash = hash;
    }

    public Musica(int id, String nome, String artista, int popular, String date, String genero, float dance, String hash) {
        this.id = id;
        this.nome = nome;
        this.artista = artista;
        this.popular = popular;
        try {
            this.date = dataConverter(date);
        } catch (Exception e) {
            Logs.Alert("Erro ao converter a data: " + e.getMessage());
        }
        this.genero = genero.split(";");
        this.dance = dance;
        this.hash = hash;
    }

    public static void iniciar(){
        setLastID(Arq.ReadLastID());
    }

    private static String[] iniciarGenero(){
        String genero [] = new String[2];
        genero[0] = "";
        genero[1]= "";
        return genero;
    } 

    public static int getLastID(){
        return LastID;
    }

    public static void setLastID(int id){
        LastID = id;
    }

    // converte a data para timestamp
    private long dataConverter(String data) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // define o formato da data
        LocalDate localDate = LocalDate.parse(data, formatter); // converte string para localdate
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant(); // converte localdate para instant
        return instant.toEpochMilli(); // retorna timestamp
    }

    // converte timestamp para data
    private String timestampConv(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp); // converte timestamp para instant
        
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate(); // converte instant para localdate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // formata a data
        return localDate.format(formatter);
    }

    public String toString() {
        return "\nID: " + id +
        "\nNome: " + nome +
        "\nArtista: " + artista + 
        "\nPopular: " + popular + 
        "\nDate: " + timestampConv(date) + 
        "\nDance: " + dance +
        "\nGenero: " + genero[0] + "; " + genero[1] + 
        "\nHash: " + hash;
    }

    

    public static Musica StringToMusica(String str){
        String[] atributos = str.split(",");
        

        if(atributos.length ==7){//construir sem ID
            Logs.Details("Musica construída sem ID");
            return new Musica(atributos[0],atributos[1],Integer.parseInt(atributos[2]),atributos[3],atributos[4],Float.parseFloat(atributos[5]),atributos[6]);
            
        }else if(atributos.length == 8){ //construir com ID
            Logs.Details("Musica construída com ID");
            try{
               return new Musica(Integer.parseInt(atributos[0]),atributos[1],atributos[2],Integer.parseInt(atributos[3]),atributos[4],atributos[5],Float.parseFloat(atributos[6]),atributos[7]); 
            }catch(NumberFormatException e){
                if(e instanceof NumberFormatException)
                    Logs.Alert("Erro na formação do Objeto\n Numero Esperado\n"+e.getMessage());
                    return null;
            }
            
        }
        throw new IllegalArgumentException("Erro ao criar a musica: atributos insuficientes ou demais.");

    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        if(this.genero.length != 2){
            throw new IllegalArgumentException("Erro ao criar o array de bytes: genero insuficiente ou demais.");
        }
        // Escreva os campos na ordem correta
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.artista);
        dos.writeInt(this.popular);
        dos.writeLong(this.date);
        String newgenero = genero[0] + ";" + genero[1];
        dos.writeUTF(newgenero);
        dos.writeFloat(this.dance);
        dos.writeUTF(this.hash);

        dos.close(); // Feche o DataOutputStream
        return baos.toByteArray(); // Retorne o array de bytes
    }

    public static Musica fromByteArray(byte[] bytes) {
    
        try {
            Musica musica = new Musica();        
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(bais);
    
            // Leia os campos na ordem correta
            musica.id = dis.readInt();
            musica.nome = dis.readUTF();
            musica.artista = dis.readUTF();
            musica.popular = dis.readInt();
            musica.date = dis.readLong();
            musica.genero = dis.readUTF().split(";");
            musica.dance = dis.readFloat();
            musica.hash = dis.readUTF();
            
            dis.close(); // Feche o DataInputStream
            return musica; // Retorne o objeto Musica            
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Ou lançar uma exceção personalizada, dependendo do seu caso de uso
        }
    }

    
}
