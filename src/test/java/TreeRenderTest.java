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

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author alexript
 */
public class TreeRenderTest {

    @Test
    public void testMainUseCase() {
        try {
            TreeRender r = new TreeRender(new String[]{"input.txt", "output.txt"});
            r.render();
        } catch (TreeRender.TreeRenderException ex) {
            fail(ex);
        }
    }

    @Test
    @Disabled
    public void testNullArgs() {
        AssertionError expectedAssertion = Assertions.assertThrows(AssertionError.class,
                () -> {
                    new TreeRender(null);
                    new TreeRender(new String[]{""});
                    new TreeRender(new String[]{"", "", ""});
                }
        );

        try {
            new TreeRender(new String[]{"", ""});
        } catch (Throwable t) {
            fail("No exception expecteed", t);
        }

    }

}
