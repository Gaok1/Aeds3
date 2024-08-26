package Store;

import Structures.MetaIndice;
import Structures.Musica;
import Tools.Arq;
import Tools.Logs;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class IndiceInvertido {

    static class GenFinder {
        int genSize;
        String genero;
        long next; // ponteiro do arquivo de musicas
        public static RandomAccessFile genFile;
        public static long BeforeReadingpos;

        void writeGenero(long pos) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(genero); // Escreve a string usando UTF-8
            dos.writeLong(next); // Escreve o valor longo
            dos.close(); // Fecha o fluxo de saída de dados

            byte[] data = baos.toByteArray(); // Obtém os bytes do fluxo de saída de dados

            genFile.seek(pos); // Move o ponteiro para a posição especificada
            genFile.writeInt(data.length); // Escreve o tamanho dos bytes
            genFile.write(data); // Escreve os bytes no arquivo
        }

        public static GenFinder readGen() {
            try {
                BeforeReadingpos = genFile.getFilePointer(); // Obtém a posição antes da leitura
                GenFinder gen = new GenFinder();
                int genSize = genFile.readInt(); // Lê o tamanho dos bytes
                byte[] b = new byte[genSize];
                genFile.readFully(b); // Lê a quantidade correta de bytes
                ByteArrayInputStream bais = new ByteArrayInputStream(b); // Cria um ByteArrayInputStream com os bytes
                                                                         // lidos
                DataInputStream dis = new DataInputStream(bais); // Cria um DataInputStream para ler os bytes
                gen.genero = dis.readUTF(); // Lê a string usando UTF-8
                gen.next = dis.readLong(); // Lê o valor longo
                return gen;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    static RandomAccessFile indFile;

    static RandomAccessFile NomeMusicaFile;
    static RandomAccessFile genFileList;
    static RandomAccessFile NomeMusicaFileList;

    public static void start() {
        try {
            indFile = new RandomAccessFile("Files/IndiceInvertido.dat", "rw");
            genFileList = new RandomAccessFile("Files/GeneroList.dat", "rw");
            GenFinder.genFile = new RandomAccessFile("Files/Genero.dat", "rw");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addIndice(MetaIndice indice) {
        try {
            Musica music = Arq.getByIndice(indice.getPosicao());
            addGenres(music.genero, indice);
            // addName(music.nome, pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addGenres(String[] generos, MetaIndice pos) {
        try {
            for (int i = 0; i < generos.length; i++) {
                GenFinder temp = isGenreInDirectory(generos[i].trim());
                if (temp != null) {
                    temp.next = putsIndexInGenreList(temp.next, pos);
                    temp.writeGenero(GenFinder.BeforeReadingpos);
                } else {
                    temp = new GenFinder();
                    temp.genero = generos[i];
                    temp.next = putsIndexInGenreList(-1, pos); // recursivamente adiciona o indice
                    temp.writeGenero(GenFinder.genFile.length());// novo genero
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static GenFinder isGenreInDirectory(String genre) {
        try {
            GenFinder.genFile.seek(0);
            while (GenFinder.genFile.getFilePointer() < GenFinder.genFile.length()) {
                GenFinder temp = GenFinder.readGen();
                if (temp.genero.trim().equals(genre.trim())) {
                    return temp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long putsIndexInGenreList(long currentPos, MetaIndice indice) {
        try {
            if (currentPos == -1) {
                return writeIndieceGen(indice);
            }
            MetaIndice thisIndice = new MetaIndice();
            genFileList.seek(currentPos + 12);
            long oldnext = genFileList.readLong();
            long next = putsIndexInGenreList(oldnext, indice);
            if (oldnext == next)
                return currentPos;

            genFileList.seek(currentPos + 12);
            genFileList.writeLong(next);
            return currentPos;
        } catch (Exception e) {
            if (e instanceof IOException) {

            }
        }
        return -1;
    }

    private static long writeIndieceGen(MetaIndice indice) {
        try {
            long retorno = genFileList.length();
            genFileList.seek(retorno);
            genFileList.writeInt(indice.getId());
            genFileList.writeLong(indice.getPosicao());
            genFileList.writeLong(-1);
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static ArrayList<MetaIndice> searchGenre(String genero) {
        ArrayList<MetaIndice> MetaIndices = new ArrayList<MetaIndice>();
        try {
            GenFinder.genFile.seek(0);
            System.out.println("Searching for genre: " + genero);
            while (GenFinder.genFile.getFilePointer() < GenFinder.genFile.length()) {
                GenFinder temp = GenFinder.readGen();
                if (temp.genero.contains(genero)) {
                    System.out.println("achei o genero");
                    searchGenreList(temp.next, MetaIndices);
                }
            }
            System.out.println("Genero não encontrado");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MetaIndices;
    }

    private static void searchGenreList(long initialPos, ArrayList<MetaIndice> indices) {
        try {
            long pos = initialPos;
            while (pos != -1) {
                genFileList.seek(pos);
                MetaIndice indice = new MetaIndice();
                indice.setId(genFileList.readInt());
                indice.setPosicao(genFileList.readLong());
                indices.add(indice);
                long next = genFileList.readLong();
                Logs.Succeed("ID: " + indice.getId() + " Pos: " + next);
                pos = next; // Atualiza a posição para a próxima entrada
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pritnGenresExistent() {
        try {
            GenFinder.genFile.seek(0);
            while (GenFinder.genFile.getFilePointer() < GenFinder.genFile.length()) {
                GenFinder temp = GenFinder.readGen();
                Logs.Details(temp.genero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}