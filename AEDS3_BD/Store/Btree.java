package Store;
import ViewTool.*;
import Tools.*;
import Structures.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;



public class Btree {

    public static class Node {
        static int order; // nessa implementação, a ordem é quantidade de chaves
        MetaIndice[] keys;
        long[] children;
        int currentKeys;
        boolean isLeaf;
        boolean isRoot;
        static RandomAccessFile file;
        long position;

        Node() {
            keys = new MetaIndice[order]; //ordem é 8
            children = new long[order + 1];
            currentKeys = 0; // + 4
            for (int i = 0; i < order; i++) { // 12 + 8 * order
                keys[i] = new MetaIndice();
                children[i] = -1;
            }
            children[order] = -1; // + 8
            isLeaf = true;
        }

        Node(long position) {
            try {
                byte[] data = new byte[Node.sizeBytes()]; // Array de bytes para armazenar os dados do nó
                file.seek(position);
                file.readFully(data); // Lê os dados do nó do arquivo para o array de bytes

                // Cria um ByteArrayInputStream com os dados lidos do arquivo
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                DataInputStream dis = new DataInputStream(bais);

                // Lê os dados do array de bytes e os atribui aos campos do nó
                currentKeys = dis.readInt();
                this.keys = new MetaIndice[order];
                this.children = new long[order + 1];
                for (int i = 0; i < order; i++) {
                    children[i] = dis.readLong();
                    keys[i] = new MetaIndice(dis.readInt(), dis.readLong());
                }
                children[order] = dis.readLong();
                isLeaf = (children[0] == -1); // Verifica se o nó é uma folha com base no primeiro filho
                this.position = position;
            } catch (Exception e) {
               // Logs.Alert("Erro ao ler Node no arquivo");
                e.printStackTrace();
            }

        }

        long getLeftSon(int i) {
            return children[i];
        }

        long getRightSon(int i) {
            return children[i + 1];
        }

        MetaIndice getBiggestKey() {
            return keys[currentKeys - 1];
        }

        MetaIndice getSmallestKey() {
            return keys[0];
        }

        MetaIndice deleteSmallestKey() {
            MetaIndice retorno = keys[0];
            for (int i = 0; i < currentKeys - 1; i++) {
                keys[i] = keys[i + 1];
                children[i] = children[i + 1];
            }
            keys[currentKeys - 1] = new MetaIndice();

            children[currentKeys - 1] = children[currentKeys];
            children[currentKeys] = -1;
            currentKeys--;
            return retorno;

        }

        void write(long position) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);

                dos.writeInt(currentKeys);
                for (int i = 0; i < order; i++) { // 12
                    dos.writeLong(children[i]);
                    dos.writeInt(keys[i].getId());
                    dos.writeLong(keys[i].getPosicao());
                }
                dos.writeLong(children[order]);

                byte[] data = baos.toByteArray();
                file.seek(position);
                file.write(data);
            } catch (Exception e) {
              //  Logs.Alert("erro ao reescreve Node no arquivo");
                e.printStackTrace();
            }
        }

        long write() {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                long retorno = file.length();
                dos.writeInt(currentKeys);
                for (int i = 0; i < order; i++) {
                    dos.writeLong(children[i]);
                    dos.writeInt(keys[i].getId());
                    dos.writeLong(keys[i].getPosicao());

                }
                dos.writeLong(children[order]);

                // Obtém os dados escritos no ByteArrayOutputStream e os escreve no arquivo na
                // posição especificada
                byte[] data = baos.toByteArray();
                file.seek(retorno);
                file.write(data);
                return retorno;
            } catch (Exception e) {
              //  Logs.Alert("Erro ao escrever Node novo no arquivo");
                e.printStackTrace();
            }
            return -1;
        }
        
        static int sizeBytes() {
            return 4 + (12 + 8) * order + 8;
        }
        boolean isLeaf() {
            for (int i = 0; i <= order; i++) {
                if (children[i] != -1) {
                    return false;
                }
            }
            return true;
        }

        void addInLeaf(MetaIndice metaIndice) { // inserçoes em folhas
            if (currentKeys == 0) { // se for o primeiro elemento
                keys[0] = metaIndice;
                currentKeys++;
                return;
            }
            for (int i = 0; i < currentKeys; i++) { // caso precise inserir no meio
                if (keys[i].getId() > metaIndice.getId()) {
                    arrastarIndices(i);
                    keys[i] = metaIndice;
                    currentKeys++;
                    return;
                }
            }
            // tem espaço e não tem q inserir no meio,logo é o maior
            keys[currentKeys] = metaIndice;
            currentKeys++;
        }

        void addInNode(MetaIndice metaIndice, long leftSon, long rightSon) {
            if (currentKeys == 0) {
                keys[0] = metaIndice;
                children[0] = leftSon;
                children[1] = rightSon;
                currentKeys++;
                return;
            }
            int i;
            for (i = 0; i < currentKeys; i++) { // caso precise inserir no meio
                if (keys[i].getId() > metaIndice.getId()) {
                    arrastarIndices(i);
                    arrastaPonteiros(i);
                    keys[i] = metaIndice;
                    children[i] = leftSon;
                    children[i + 1] = rightSon;
                    currentKeys++;
                    break; // Sai do loop após inserir o novo índice
                }
            }
            if (i == currentKeys) { // Se o novo índice não puder ser inserido no meio
                keys[currentKeys] = metaIndice;
                children[currentKeys] = leftSon;
                children[currentKeys + 1] = rightSon;
                currentKeys++;
            }
        }

        void arrastarIndices(int posicao) {
            for (int i = currentKeys; i > posicao; i--) {
                keys[i] = keys[i - 1];
            }
        }
        void arrastaPonteiros(int posicao) {
            for (int i = currentKeys + 1; i > posicao; i--) {
                children[i] = children[i - 1];
            }
        }
    }

    static RandomAccessFile file;
    static RandomAccessFile printFile;
    static long rootP;

    public static void start() {

        try {
            Node.order = 8;
            file = new RandomAccessFile("Files/btree.dat", "rw");
            printFile = new RandomAccessFile("Files/printBtree.info", "rw");
            Node.file = file;
            if (file.length() <= 8) {
                file.writeLong(8);// raiz
                Node root = new Node();
                root.write(8);
                rootP = 8;
            } else {
                rootP = file.readLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRoot() {
        try {
            file.seek(0);
            file.writeLong(rootP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void add(MetaIndice metaIndice) {
        Node NodeRoot = new Node(rootP);
       // Logs.Details("inserindo em BTREE");
        add(metaIndice, NodeRoot, rootP);
    }

    static void printTree() {
        try {
            printFile.setLength(0);
            String startPrint = "======== IMPRINMINDO Root: " + rootP + "\n";
            printFile.writeChars(startPrint);
            printTree(rootP, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void printTree(long position, int level) {
        String NodeStr = " Level: " + level + " - ";
        NodeStr += position + " -> |";
        Node node = new Node(position);

        for (int i = 0; i < Node.order; i++) {
            NodeStr += node.children[i] + "/";
            NodeStr += node.keys[i].getId() + ":::";
        }
        NodeStr += node.children[Node.order] + "|\n";
        try {
            printFile.writeChars(NodeStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (node.isLeaf()) {
            return;
        }
        for (int i = 0; i < node.currentKeys + 1; i++) {
            if (node.children[i] != -1)
                printTree(node.children[i], level + 1);
        }
    }

    private static long add(MetaIndice metaIndice, Node node, long Nodeposition) {
        if (node.isLeaf()) {// se for folha, devemos inserir o indice
            if (node.currentKeys == Node.order) {
                long newNodePosition = splitNode(node, Nodeposition); // no com metade dos elementos copiados
                Node newNode = new Node(newNodePosition);
                newNode.addInLeaf(metaIndice);
                MetaIndice subindo = newNode.deleteSmallestKey();
                newNode.write(newNodePosition);
                subir(subindo, newNodePosition, Nodeposition);
                return Nodeposition;
            }
            if (node.currentKeys < Node.order) {
                node.addInLeaf(metaIndice); // adiciona o indice na memoria secundaria
                node.write(Nodeposition); // atualiza o No no arquivo
                return Nodeposition;
            }
        }
        /* continue procurando */
        long nextNodePosition = -1;
        for (int i = 0; i < Node.order; i++) { // escolher o filho para continuar a busca
            if (node.keys[i].getId() > metaIndice.getId()) {
                nextNodePosition = node.getLeftSon(i);// insere na esquerda do maior
                break;
            } else if (node.keys[i].getId() == -1) {
                nextNodePosition = node.getLeftSon(i);// insere na direita do maior/esquerda do null
                break;
            } else if (i == Node.order - 1) {
                nextNodePosition = node.getRightSon(i);// insere na direita do maior
                break;
            }
        }
        Node nextNode = new Node(nextNodePosition);
        Nodeposition = add(metaIndice, nextNode, nextNodePosition);
        return Nodeposition;
    }

    static long splitNode(Node node, long Nodeposition) {
       // Logs.Details("Split Node");
        // Criar novo nó e copiar metade dos índices e ponteiros
        Node newNode = new Node();
        for (int i = 0; i < Node.order / 2; i++) { // copiar metade dos elementos
            newNode.keys[i] = node.keys[i + Node.order / 2];
            node.keys[i + Node.order / 2] = new MetaIndice();
            node.currentKeys--;
            newNode.currentKeys++;
        }

        if (!node.isLeaf()) {
            int j = 1;
            for (int i = Node.order / 2 + 1; i < Node.order + 1; i++) {
                newNode.children[j] = node.children[i];
                node.children[i] = -1;
                j++;
            }
        }
        node.write(Nodeposition);
        return newNode.write();
    }

    static void subir(MetaIndice elementoSubindo, long newNodePosition, long Nodeposition) {
      //  Logs.Details("Subindo elemento na arvore");
        if (Nodeposition == rootP) { 
            Node newRoot = new Node();
            newRoot.addInNode(elementoSubindo, Nodeposition, newNodePosition);
            newRoot.isRoot = true;
            rootP = newRoot.write();
            updateRoot();
            return;
        }
        Node father = getFather(Nodeposition);
        if (father != null && !father.isRoot) {
            if (father.currentKeys < Node.order) {// se o pai n estiver cheio
                father.addInNode(elementoSubindo, Nodeposition, newNodePosition);
                father.write(father.position);

            } else { // pai ta cheio (split no pai)
                long newFatherBrother = splitNode(father, father.position);
                Node fatherBrother = new Node(newFatherBrother);
                fatherBrother.addInNode(elementoSubindo, Nodeposition, newNodePosition);
                MetaIndice subindo = fatherBrother.deleteSmallestKey();
                // so par ter certeza
                fatherBrother.write(newFatherBrother); // atualiza o irmao
                subir(subindo, newFatherBrother, father.position);

            }
        } else { // não tem pai (nivel da raiz)
            Node newRoot = new Node();
            newRoot.addInNode(elementoSubindo, Nodeposition, newNodePosition);
            newRoot.isRoot = true;
            rootP = newRoot.write();
            updateRoot();
        }
    }

    static Node getFather(long Nodeposition) {
        Node NodeRoot = new Node(rootP);
        NodeRoot.position = rootP;
        return getFather(Nodeposition, NodeRoot);
    }

    static Node getFather(long NodepositionTarget, Node searchtemp) {
        if (searchtemp.isLeaf()) {
            return null;
        }
        for (int i = 0; i < searchtemp.currentKeys + 1; i++) {// procurar nos filhos (chaves + 1)
            if (searchtemp.children[i] == NodepositionTarget) {
                if (NodepositionTarget == rootP) {
                    searchtemp.isRoot = true;
                    return searchtemp;
                }
                return searchtemp;
            }
        }
        // não é pai do filho, procurar nos filhos
        for (int i = 0; i < searchtemp.currentKeys; i++) {
            if (searchtemp.keys[i].getId() > NodepositionTarget) {
                Node nextSearchTemp = new Node(searchtemp.children[i]);
                nextSearchTemp.position = searchtemp.children[i];
                return getFather(NodepositionTarget, nextSearchTemp);
            } else if (i == searchtemp.currentKeys - 1) {
                Node nextSearchTemp = new Node(searchtemp.children[i + 1]);
                nextSearchTemp.position = searchtemp.children[i + 1];
                return getFather(NodepositionTarget, nextSearchTemp);
                
            }
        }
        return null;
    }

    public static MetaIndice search(int id) {
        Node NodeRoot = new Node(rootP);
        return search(id, NodeRoot, 0);
    }

    static MetaIndice search(int id, Node node, int level) {
        for (int i = 0; i < node.currentKeys; i++) {
            if (node.keys[i].getId() == id) {
                String message = "Encontrado: " + node.keys[i].getId() + " na altura: " + level ;
              //Logs.Succeed(message);
                return node.keys[i];
            }
            if (node.keys[i].getId() > id) {
                if (node.isLeaf()) {
                    return null;
                }
                Node nextNode = new Node(node.children[i]);
                return search(id, nextNode, level + 1);
            }
        }
        if (node.isLeaf()) {
            return null;
        }
        Node nextNode = new Node(node.children[node.currentKeys]);
        return search(id, nextNode, level + 1);
    }

    public static void updateIndex(MetaIndice metaIndice) {
        Node root = new Node(rootP);
        updateIndex(metaIndice, root, rootP);
    }

    static MetaIndice updateIndex(MetaIndice metaIndice, Node node, long Nodeposition) {
        for (int i = 0; i < node.currentKeys; i++) {
            if (node.keys[i].getId() == metaIndice.getId()) {
                node.keys[i].setPosicao(metaIndice.getPosicao());
                node.write(Nodeposition);

            }
            if (node.keys[i].getId() > metaIndice.getId()) {
                if (node.isLeaf()) {
                    return null;
                }
                Node nextNode = new Node(node.children[i]);
                return updateIndex(metaIndice, nextNode, node.children[i]);
            }
        }
        if (node.isLeaf()) {
            return null;
        }
        Node nextNode = new Node(node.children[node.currentKeys]);//for não alcança o ultimo filho
        return updateIndex(metaIndice, nextNode, node.children[node.currentKeys]);
    }

}
