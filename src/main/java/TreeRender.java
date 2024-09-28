/*
 * The MIT License
 *
 * Copyright 2024 alexript.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author alexript
 */
public class TreeRender {

    private File inputFile;
    private File outputFile;

    protected TreeRender(String[] args) throws TreeRenderException {
        parseArgs(args);
    }

    /**
     * Parse commandline arguments. Check file names and basic file access
     * ensurance.
     *
     * @param args non-null array of arguments
     */
    private void parseArgs(String[] args) throws TreeRenderException {
        assert args != null : "null as arguments array";
        String inputFileName = args[0];
        String outputFileName = args[1];

        try {
            inputFile = new File(inputFileName);
            if (!inputFile.isFile() || !inputFile.canRead()) {
                String msg = String.format("Unable to read input file '%s'", inputFileName);
                throw new TreeRenderException(msg);
            }

        } catch (NullPointerException ex) {
            throw new TreeRenderException("Unexpected null argument as input file name.");
        }

        try {
            outputFile = new File(outputFileName);
            if (outputFile.exists() && !outputFile.canWrite()) {
                String msg = String.format("Output file '%s' can not be written.", outputFileName);
                throw new TreeRenderException(msg);
            }
        } catch (NullPointerException ex) {
            throw new TreeRenderException("Unexpected null argument as output file name.");
        }
    }

    private static class Node<T> extends LinkedList<Node<T>> {

        public T value;

        public Node(T value) {
            super();
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }

    }

    /**
     * Recursive parser of input data
     *
     * @param node current parent node
     * @param r StringReader
     * @throws IOException on string read error
     */
    private void parse(Node<Integer> node, StringReader r) throws IOException {
        if (node == null) {
            return;
        }
        int ch;
        StringBuilder sb = null;
        Node<Integer> currentNode = null;
        while (-1 != (ch = r.read())) {
            if (ch == '(') {
                if (sb != null) {
                    final String str = sb.toString();
                    if (!str.isBlank()) {
                        Integer i = Integer.parseInt(str);
                        currentNode = new Node<>(i);
                        node.add(currentNode);
                    }
                    sb = new StringBuilder();
                }
                parse(currentNode, r);
            } else if (ch == ')') { // end of branch
                if (sb != null) {
                    final String str = sb.toString();
                    if (!str.isBlank()) {
                        Integer i = Integer.parseInt(str);
                        Node<Integer> n = new Node<>(i);
                        node.add(n);
                    }
                    sb = new StringBuilder();
                }

                return;
            } else if (Character.isDigit(ch)) { // read digit to number
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append((char) ch);
            } else { // any separator (not digit and not parenthesis)
                if (sb != null) {
                    final String str = sb.toString();
                    if (!str.isBlank()) {
                        Integer i = Integer.parseInt(str);
                        currentNode = new Node<>(i);
                        node.add(currentNode);
                        sb = new StringBuilder();
                    }
                }
            }
        }

    }

    /**
     * Read data from input file and build tree
     */
    private Node<Integer> readInput() throws TreeRenderException {
        Node<Integer> root = new Node<>(null);
        try {
            List<String> lines = Files.readAllLines(inputFile.toPath());
            String text = String.join("", lines);
            // System.out.println(text);
            try (StringReader r = new StringReader(text.trim())) {
                int ch;
                while (-1 != (ch = r.read())) {
                    if (ch == '(') {
                        break;
                    }
                }
                if (ch != -1) {
                    parse(root, r);
                }
            }

        } catch (IOException ex) {
            throw new TreeRenderException("Input file error", ex);
        }
        return root;
    }

    /**
     * Draw Node's tree into output file
     *
     * @param rootNode root node
     */
    private void renderOutput(Node<Integer> rootNode) throws TreeRenderException {
        try (FileWriter out = new FileWriter(outputFile)) {

            int level = 0;
            outNode(out, rootNode, "", 0, rootNode.size(), false);
        } catch (IOException ex) {
            throw new TreeRenderException("Output file error", ex);
        }
    }

    private String extendString(int d, String ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < d; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Recursive write subnodes
     *
     * @param out writer
     * @param current current node
     * @param level tree level
     */
    private void outNode(FileWriter out, Node<Integer> current, String prefix, int lenCorr, int chsize, boolean last) throws IOException {
        Integer value = current.getValue();
        if (value != null) {

            final String val = value.toString();
            int vallen = val.length();
            String suffix = current.size() > 0 ? extendString(lenCorr - vallen, "-") + "---+" : "";

            final String line = String.format("%s%s%s\n", prefix, val, suffix);
        //    System.out.print(line);
            out.write(line);
        }
        int maxLen = 0;
        for (Node<Integer> n : current) {
            String val = n.toString();
            maxLen = Math.max(maxLen, val.length());
        }

        int currSize = current.size();
        int i = 0;
        for (Node<Integer> n : current) {

            outNode(out, n, prefix + (value == null ? "" : (chsize > 1 && !last ? "|   " : "    ")) + extendString(lenCorr - 1, " "), maxLen, currSize, i>=currSize-1);
            i++;
        }
    }

    /**
     * REnder input file data into aoutput file as pseudographical reprsentation
     *
     * @throws TreeRender.TreeRenderException when render() errors
     */
    public void render() throws TreeRenderException {
        Node<Integer> rootNode = readInput();
        renderOutput(rootNode);
    }

    public static class TreeRenderException extends Exception {

        public TreeRenderException(String message) {
            super(message);
        }

        public TreeRenderException(String message, Throwable t) {
            super(message, t);
        }
    }

    /**
     * Appliaction standart entry point
     *
     * @param args commandline arguments
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Wrong commandline arguments. Expected: input fiele name and output file name.");
            return;
        }

        TreeRender renderer;
        try {
            renderer = new TreeRender(args);
        } catch (TreeRenderException ex) {
            System.err.println(String.format("Initialisation error: %s", ex.getMessage()));
            return;
        }
        try {
            renderer.render();
        } catch (TreeRenderException ex) {
            System.err.println(String.format("Render error: %s", ex.getMessage()));
        }

        System.out.println("TreeRender done.");
    }
}
