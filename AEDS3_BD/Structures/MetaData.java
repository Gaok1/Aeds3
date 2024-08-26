package Structures;
import Tools.Logs;
import java.io.*;
import java.io.RandomAccessFile;

public class MetaData {
    public boolean lapide;
    public int sizeBytes;
    public RandomAccessFile raf;

    


    public MetaData(RandomAccessFile raf) {
        this.lapide = false;
        this.sizeBytes = 0;
        this.raf = raf;
        Logs.Succeed("Compartilhamento de arquivo com metadados estabelecido!");
        Logs.Succeed("Metadados criados com sucesso!");

    }

    public Boolean readMetaData() {
        try {
            this.lapide = raf.readBoolean(); // Lê a lapide
            this.sizeBytes = raf.readInt(); // Lê o tamanho do registro da música
            return true;
        } catch (Exception e) { // se for end of file
            if (e instanceof java.io.EOFException)
                Logs.KindaAlert("MetaDados estão em EOF!");
            else
                Logs.Alert("Erro ao ler os metadados: " + e.getMessage());

        }
        return false;
    }

    public void writeMetaData(int sizebytes) {
        try {
            raf.writeBoolean(false);
            raf.writeInt(sizebytes);
        } catch (IOException e) {
            Logs.Alert("Erro ao escrever os metadados: " + e.getMessage());
        }
    }

    void WriteSize(int size) {
        try {
            raf.writeInt(size);
        } catch (IOException e) {
            Logs.Alert("Erro ao escrever o tamanho: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Lapide: " + lapide + "\nTamanho: " + sizeBytes;
    }
}
