package Compact;

import Tools.*;
import java.io.*;
import java.util.*;

public class Huffman {
    private static String local = "Compact/Huffman/";
    private static RandomAccessFile raf;

    // Armazena os códigos de Huffman para cada byte
    private static Map<Byte, HuffmanNode> huffmanNodes;
    private static HuffmanNode root;

    /**
     * Inicia a leitura do arquivo e compactação dos registros.
     * 
     * @param file O caminho para o arquivo a ser compactado.
     */
    public static void start(String file) {
        try {
            raf = new RandomAccessFile(file, "r");
            raf.seek(4); // Pula os primeiros 4 bytes
    
            // Lê todos os registros do arquivo e calcula as frequências dos bytes
            Map<Byte, Integer> frequencyMap = readByteFrequencies();
    
            // Constrói a árvore de Huffman
            buildHuffmanTree(frequencyMap);
    
            // Inicializa o mapa de HuffmanNodes
            huffmanNodes = new HashMap<>();
            generateHuffmanCodes(root, "", huffmanNodes);
    
            // Escreve a árvore de Huffman em um arquivo separado
            writeHuffmanTreeToFile(local + "huffman_tree.huff");
    
            // Compacta os dados
            byte[] compressedData = compressFile(file);
    
            // Escreve os dados compactados em um novo arquivo
            writeCompressedFile(compressedData);
    
        } catch (Exception e) {
            //System.out.println("Erro ao abrir o arquivo");
            e.printStackTrace();
        }
    }
    
    

    /**
     * Lê todos os bytes únicos do arquivo.
     * 
     * @return Conjunto de bytes únicos presentes no arquivo.
     */
    private static Map<Byte, Integer> readByteFrequencies() throws IOException {
        Map<Byte, Integer> frequencyMap = new HashMap<>();
        int blockSize = 1024;
        byte[] buffer = new byte[blockSize];
    
        while (true) {
            int bytesRead = raf.read(buffer);
            if (bytesRead == -1) break; // Fim do arquivo
    
            for (int i = 0; i < bytesRead; i++) {
                byte b = buffer[i];
                frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
            }
        }
    
        return frequencyMap;
    }

    /**
     * Constrói a árvore de Huffman a partir dos dados fornecidos.
     * 
     * @param data Os bytes únicos a serem processados.
     */
    private static void buildHuffmanTree(Map<Byte, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
    
        for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
    
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
    
            HuffmanNode newNode = new HuffmanNode((byte) 0, left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
    
            priorityQueue.add(newNode);
        }
    
        root = priorityQueue.poll();
    }
    private static void walkInTree(HuffmanNode root){
        if(root == null) return;
        if(root.left == null && root.right == null){
            //System.out.println(root.data + " " + root.code);
        }
        walkInTree(root.left);
        walkInTree(root.right);
    }

    /**
     * Gera os códigos de Huffman a partir da árvore construída.
     * 
     * @param node O nó atual na árvore de Huffman.
     * @param code O código de Huffman acumulado até agora.
     * @param huffmanNodes O mapa que armazena os nós de Huffman.
     */
    private static void generateHuffmanCodes(HuffmanNode node, String code, Map<Byte, HuffmanNode> huffmanNodes) {
        if (node == null) return;
    
        if (node.left == null && node.right == null) {
            node.code = code;
            huffmanNodes.put(node.data, node);
            //System.out.println("Byte: " + node.data + ", Código: " + node.code);
        } else {
            generateHuffmanCodes(node.left, code + "0", huffmanNodes);
            generateHuffmanCodes(node.right, code + "1", huffmanNodes);
        }
    }
    

    /**
     * Compacta o arquivo utilizando os códigos de Huffman.
     * 
     * @param file O caminho para o arquivo a ser compactado.
     * @return Os dados compactados em um array de bytes.
     */
    public static byte[] compressFile(String file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos);
        int block = 1024;
        raf.seek(0);

        while (raf.getFilePointer() < raf.length()) {
            byte[] record = new byte[block];
            int bytesRead = raf.read(record);
            for (int i = 0; i < bytesRead; i++) {
                byte b = record[i];
                HuffmanNode node = huffmanNodes.get(b);
                if (node != null) {
                    for (char bit : node.code.toCharArray()) {
                        bos.writeBit(bit == '1' ? 1 : 0);
                    }
                }else{
                    //System.out.println("null");
                }
            }
        }
        return baos.toByteArray();
    }

    /**
     * Escreve a árvore de Huffman em um arquivo separado.
     * 
     * @param file O caminho para o arquivo onde a árvore será escrita.
     */
    private static void writeHuffmanTreeToFile(String file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             DataOutputStream dos = new DataOutputStream(fos)) {
            writeHuffmanTree(root, dos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escreve a árvore de Huffman no fluxo de saída.
     * 
     * @param node O nó atual na árvore de Huffman.
     * @param dos O fluxo de saída onde a árvore será escrita.
     * @throws IOException Se ocorrer um erro de entrada/saída.
     */
    private static void writeHuffmanTree(HuffmanNode node, DataOutputStream dos) throws IOException {
        if (node == null) {
            dos.writeBoolean(true);
            return;
        }
        dos.writeBoolean(false);
        dos.writeByte(node.data);
        dos.writeUTF(node.code == null ? "" : node.code); // Corrige possível null pointer
        writeHuffmanTree(node.left, dos);
        writeHuffmanTree(node.right, dos);
    }

    /**
     * Lê a árvore de Huffman do fluxo de entrada.
     * 
     * @param dis O fluxo de entrada de onde a árvore será lida.
     * @return O nó raiz da árvore de Huffman.
     * @throws IOException Se ocorrer um erro de entrada/saída.
     */
    private static HuffmanNode readHuffmanTree(DataInputStream dis) throws IOException {
        if (dis.readBoolean()) {
            return null;
        }
        HuffmanNode node = new HuffmanNode(dis.readByte(), 0);
        node.code = dis.readUTF();
        node.left = readHuffmanTree(dis);
        node.right = readHuffmanTree(dis);
        return node;
    }
    /**
     * Escreve os dados compactados em um novo arquivo.
     * 
     * @param compressedData Os dados compactados.
     */
    private static void writeCompressedFile(byte[] compressedData) {
        try {
            FileOutputStream fos = new FileOutputStream(local + "compressed_file.huff");
            fos.write(compressedData);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Descompacta os dados do arquivo compactado.
     * 
     * @param compressedFile O caminho para o arquivo compactado.
     * @param treeFile O caminho para o arquivo da árvore de Huffman.
     * @param outputFile O caminho para o arquivo descompactado.
     */
    public static void decompress() {
        File compressedFile = new File(local + "compressed_file.huff");
        File treeFile = new File(local + "huffman_tree.huff");
        File outputFile = new File(local + "decompressed_file.txt");
        try {
            // Lê a árvore de Huffman do arquivo
            FileInputStream fis = new FileInputStream(treeFile);
            DataInputStream dis = new DataInputStream(fis);
            root = readHuffmanTree(dis);
            dis.close();
    
            // Lê os dados compactados e descompacta
            fis = new FileInputStream(compressedFile);
            BitInputStream bis = new BitInputStream(fis);
    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HuffmanNode currentNode = root;
    
            while (true) {
                int bit = bis.readBit();
                if (bit == -1) break; // Fim do arquivo
                currentNode = (bit == 1) ? currentNode.right : currentNode.left;
                // Se o nó é uma folha
                if (currentNode.left == null && currentNode.right == null) {
                    baos.write(currentNode.data);
                    currentNode = root;
                }
            }
            bis.close();
            // Escreve os dados descompactados em um novo arquivo
            FileOutputStream fos = new FileOutputStream(outputFile);
            baos.writeTo(fos);
            fos.close();
    
            // Verificação de depuração
            //System.out.println("Descompressão concluída. Dados salvos em: " + outputFile.getAbsolutePath());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

/**
 * Classe que representa um nó na árvore de Huffman.
 */
class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    byte data;
    String code;
    HuffmanNode left, right;

    /**
     * Construtor para criar um nó de Huffman.
     * 
     * @param data O byte representado pelo nó.
     * @param frequency A frequência do byte.
     */
    HuffmanNode(byte data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}

/**
 * Classe para manipulação de bits em um fluxo de saída.
 */
class BitOutputStream {
    private ByteArrayOutputStream baos;
    private int currentByte;
    private int numBitsFilled;

    public BitOutputStream(ByteArrayOutputStream baos) {
        this.baos = baos;
        this.currentByte = 0;
        this.numBitsFilled = 0;
    }

    public void writeBit(int bit) throws IOException {
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1");
        }
        currentByte = (currentByte << 1) | bit;
        numBitsFilled++;
        if (numBitsFilled == 8) {
            baos.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
    }

    public void close() throws IOException {
        if (numBitsFilled > 0) {
            currentByte <<= (8 - numBitsFilled);
            baos.write(currentByte);
        }
        baos.close();
    }
}

/**
 * Classe para manipulação de bits em um fluxo de entrada.
 */
class BitInputStream {
    private InputStream input;
    private int currentByte;
    private int numBitsRemaining;

    public BitInputStream(InputStream input) {
        this.input = input;
        this.currentByte = 0;
        this.numBitsRemaining = 0;
    }

    public int readBit() throws IOException {
        if (currentByte == -1) {
            return -1;
        }
        if (numBitsRemaining == 0) {
            currentByte = input.read();
            if (currentByte == -1) {
                return -1;
            }
            numBitsRemaining = 8;
        }
        numBitsRemaining--;
        return (currentByte >> numBitsRemaining) & 1;
    }

    public int available() throws IOException {
        return input.available();
    }

    public void close() throws IOException {
        input.close();
    }
}
