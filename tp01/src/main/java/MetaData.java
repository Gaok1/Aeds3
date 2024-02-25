import java.io.*;
import java.io.RandomAccessFile;
public class MetaData {
    boolean lapide;
    int sizeBytes;
    RandomAccessFile raf;

    MetaData() {
        this.lapide = false;
        this.sizeBytes = 0;
    }

    MetaData(RandomAccessFile raf) {
        this.lapide = false;
        this.sizeBytes = 0;
        this.raf = raf;

    }

    void readMetaData() {
        try {
            this.lapide = raf.readBoolean(); // Lê a lapide
            this.sizeBytes = raf.readInt(); // Lê o tamanho do registro da música
        } catch (IOException e) {
            System.out.println("Erro ao ler os metadados: " + e.getMessage());
        }
    }


    void writeMetaData(int sizebytes) {
        try {
            raf.writeBoolean(false);
            raf.writeInt(sizebytes);
        } catch (IOException e) {
            System.out.println("Erro ao escrever os metadados: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Lapide: " + lapide + "\nTamanho: " + sizeBytes;
    }
}
