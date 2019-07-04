/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot3;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Chris
 */
public class Test {

    private static int maxI = 20;

    public static void main(String[] args) {
        Myi z = new Myi(1, 0, false);
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        // evaluate JavaScript code from String
        Object obj = null;
        try {
            obj = engine.eval("new Scanner(new File(\"hi.txt\"))");
        } catch (ScriptException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(obj);
    }

    public static Myi PartOfMand(double a, double y) {
        Myi tot = new Myi(a, y, false);
        int i = 0;
        while (i < maxI) {
            tot = tot.power(2);
            tot = tot.add(a, y);
            i++;
        }
        return tot;
    }

}
