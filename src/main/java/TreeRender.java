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

    /**
     * Read data from input file and build tree
     */
    private void readInput() {

    }

    /**
     * REnder input file data into aoutput file as pseudographical reprsentation
     */
    public void render() {
        readInput();

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
        renderer.render();
    }
}
