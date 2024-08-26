package Structures;
import java.io.RandomAccessFile;

public class MetaIndice {
    private int id;
    private long posicao;


    public MetaIndice(int id, long posicao) {
        this.id = id;
        this.posicao = posicao;
    }
    public MetaIndice() {
        this.id = -1;
        this.posicao = -1;
    }

    void read(RandomAccessFile file)throws Exception {
        try {
            this.id = file.readInt();
            this.posicao = file.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void write(RandomAccessFile file) throws Exception {
        try {
            file.writeInt(this.id);
            file.writeLong(this.posicao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int sizeBytes(){
        return 4 + 8;
    }

    public int getId() {
        return id;
    }

    public long getPosicao() {
        return posicao;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setPosicao(long posicao) {
        this.posicao = posicao;
    }
}