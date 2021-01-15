package uniolunisaar.adam.logic.ui.cl.modules;

import java.io.PrintWriter;
import org.apache.commons.cli.HelpFormatter;
import uniolunisaar.adam.logic.ui.cl.AdamUI;

/**
 *
 * @author Manuel Gieseking
 */
public abstract class Modules {

    public abstract AbstractModule[] getModules();

    public void printPossibleModules(PrintWriter pw) {
        HelpFormatter formatter = new HelpFormatter();
        // Figure out the length of the longest module name
        int longestModuleName = 0;
        for (AbstractModule module : getModules()) {
            longestModuleName = Math.max(longestModuleName, module.getName().length());
        }
//        String format = "  %-" + Integer.toString(longestModuleName) + "s  %s";
        String format = "  %-" + Integer.toString(longestModuleName) + "s  ";
        pw.append("Usage: ./" + getToolName() + " <module> or java -DPROPERTY_FILE=./ADAM.properties -jar " + getToolName() + ".jar <module>");
        pw.append(System.lineSeparator());
        pw.append("Available modules:");
        pw.append(System.lineSeparator());
        int start = longestModuleName + 4;
        for (AbstractModule module : getModules()) {
//            System.out.println(String.format(format, module.getName(), module.getDescr()));
//            System.out.print(String.format(format, module.getName()));

//            formatter.printWrapped(pw, 90 - start, start,
//                    String.format(format, module.getName()) + module.getDescr());
            formatter.printWrapped(pw, AdamUI.CONSOLE_WIDTH, start,
                    String.format(format, module.getName()) + module.getDescr());
            pw.flush();
        }
    }

    public String getToolName() {
        return "adam";
    }
}
