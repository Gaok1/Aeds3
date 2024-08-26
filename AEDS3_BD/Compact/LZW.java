package Compact;

import Tools.*;
import java.io.*;
import java.util.*;

public class LZW {

    private static RandomAccessFile raf;

    /**
     * Inicia a leitura do arquivo e compactação dos registros.
     * 
     * @param inputFile  O caminho para o arquivo a ser compactado.
     * @param outputFile O caminho para o arquivo compactado.
     */
    public static void start(String inputFile, String outputFile) {
        try {
            raf = new RandomAccessFile(inputFile, "r");
            raf.seek(4); // Pula os primeiros 4 bytes

            // Lê todos os registros do arquivo e armazena em uma lista
            List<byte[]> records = readAllRecords();

            // Concatena todos os registros em um único array de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (byte[] record : records) {
                baos.write(record);
                baos.write((byte) 0); // Adiciona um delimitador nulo entre registros
            }
            byte[] allData = baos.toByteArray();
            
            // Compacta os dados
            List<Integer> compressedData = compressBytes(allData);

            // Escreve os dados compactados em um novo arquivo
            writeCompressedFile(compressedData, outputFile);

        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo");
            e.printStackTrace();
        }
    }

    /**
     * Lê todos os registros do arquivo.
     * 
     * @return Lista de registros como arrays de bytes.
     */
    private static List<byte[]> readAllRecords() throws IOException {
        List<byte[]> records = new ArrayList<>();
        while (raf.getFilePointer() < raf.length()) {
            boolean lapide = raf.readBoolean();
            int size = raf.readInt();
            byte[] record = new byte[size];
            raf.readFully(record);
            if (!lapide) {
                records.add(record);
            }
        }
        return records;
    }

    /**
     * Compressão LZW de um array de bytes.
     * 
     * @param input O array de bytes a ser comprimido.
     * @return A lista de códigos inteiros representando os dados comprimidos.
     */
    private static List<Integer> compressBytes(byte[] input) {
        int dictSize = 256;
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);
        }

        String w = "";
        List<Integer> result = new ArrayList<>();
        for (byte b : input) {
            String wc = w + (char) (b & 0xFF);
            if (dictionary.containsKey(wc)) {
                w = wc;
            } else {
                result.add(dictionary.get(w));
                dictionary.put(wc, dictSize++);
                w = "" + (char) (b & 0xFF);
            }
        }

        if (!w.equals("")) {
            result.add(dictionary.get(w));
        }
        return result;
    }

    /**
     * Escreve os dados compactados em um novo arquivo.
     * 
     * @param compressedData A lista de códigos inteiros representando os dados
     *                       compactados.
     * @param outputFile     O caminho para o arquivo compactado.
     */
    private static void writeCompressedFile(List<Integer> compressedData, String outputFile) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            for (Integer code : compressedData) {
                if (code != null) {
                    dos.writeInt(code);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Descompacta os dados do arquivo compactado.
     * 
     * @param compressedFile O caminho para o arquivo compactado.
     * @param outputFile     O caminho para o arquivo descompactado.
     */
    public static void decompress(String compressedFile, String outputFile) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(compressedFile))) {
            List<Integer> compressed = new ArrayList<>();
            try {
                while (true) {
                    compressed.add(dis.readInt());
                }
            } catch (EOFException e) {
                // Fim do arquivo alcançado
            }

            byte[] decompressed = decompressBytes(compressed);

            // Escreve os registros descompactados no arquivo de saída
            writeDecompressedFile(decompressed, outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escreve os registros descompactados em um novo arquivo.
     * 
     * @param decompressedData O array de bytes descomprimido representando os dados.
     * @param outputFile       O caminho para o arquivo descompactado.
     */
    private static void writeDecompressedFile(byte[] decompressedData, String outputFile) {
        try (RandomAccessFile rafOut = new RandomAccessFile(outputFile, "rw")) {
            // Escreve o cabeçalho (por exemplo, o UID)
            rafOut.writeInt(0); // Placeholder para o UID, ajuste conforme necessário

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            for (byte b : decompressedData) {
                if (b == 0) {
                    byte[] record = buffer.toByteArray();
                    if (record.length > 0) {
                        rafOut.writeBoolean(false); // Não é uma lápide
                        rafOut.writeInt(record.length); // Tamanho do registro
                        rafOut.write(record); // Dados do registro
                    }
                    buffer.reset();
                } else {
                    buffer.write(b);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Descompressão LZW de uma lista de códigos inteiros.
     * 
     * @param compressed A lista de códigos inteiros representando os dados
     *                   comprimidos.
     * @return O array de bytes descomprimido.
     */
    private static byte[] decompressBytes(List<Integer> compressed) {
        int dictSize = 256;
        Map<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dictionary.put(i, "" + (char) i);
        }

        String w = "" + (char) (int) compressed.remove(0);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        result.write(w.getBytes(), 0, w.length());

        for (int k : compressed) {
            String entry;
            if (dictionary.containsKey(k)) {
                entry = dictionary.get(k);
            } else if (k == dictSize) {
                entry = w + w.charAt(0);
            } else {
                throw new IllegalArgumentException("Código inválido: " + k);
            }

            result.write(entry.getBytes(), 0, entry.length());

            // Adiciona a nova entrada ao dicionário
            if (dictSize < Integer.MAX_VALUE) {
                dictionary.put(dictSize++, w + entry.charAt(0));
            }

            w = entry;
        }
        return result.toByteArray();
    }

}
