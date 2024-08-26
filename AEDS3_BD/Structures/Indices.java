//classe dedicada para fazer indices dos arquivos com base em ID's
package Structures;
import java.io.RandomAccessFile;

/**
 * Indices
 */
public class Indices {
    // classe para coordenar o arquivo de indices
    /**
     * InnerIndices
     */

    private static RandomAccessFile file;

    Indices(String path) {
        try {
            file = new RandomAccessFile(path, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start(String path) {
        try {
            file = new RandomAccessFile(path, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static MetaIndice findIndice(int id) {// hope never use this, cause its O(n)
        try {
            file.seek(0);
            while (file.getFilePointer() < file.length()) {
                int currentId = file.readInt();
                long currentPos = file.readLong();
                if (currentId == id) {
                    return new MetaIndice(currentId, currentPos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeIndice(int id, long pos) {
        try {
            // pos is the position of the record in the file
            file.seek(file.length());
            file.writeInt(id);
            file.writeLong(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeIndiceString(String name, long pos, RandomAccessFile fileIndice) {
        try {
            // pos is the position of the record in the file
            fileIndice.seek(fileIndice.length());
            fileIndice.writeUTF(name);
            fileIndice.writeLong(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    


    public static MetaIndice[] getAllIndices(String path) {
        try {
            file = new RandomAccessFile(path, "rw");
            file.seek(0);
            // Lê os primeiros 12 índices inválidos
            for (int i = 0; i < 12; i++) {
                file.readInt();
                file.readLong();
            }
    
            // Calcula o tamanho do array de índices
            int totalIndices = (int) ((file.length() - 12 * 12) / 12);
            MetaIndice[] indices = new MetaIndice[totalIndices];
    
            // Lê os índices válidos e os armazena no array
            for (int i = 0; i < totalIndices; i++) {
                int id = file.readInt();
                long offset = file.readLong();
                indices[i] = new MetaIndice(id, offset);
            }
            return indices;
        } catch (Exception e) {
            if (e instanceof java.io.EOFException) {
                // Caso o arquivo tenha menos de 12 índices
                return new MetaIndice[0];
            }
        }
        return null;
    }
    

}
