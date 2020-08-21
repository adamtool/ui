package uniolunisaar.adam.logic.ui.cl;

import java.io.PrintWriter;
import java.util.Arrays;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uniolunisaar.adam.logic.ui.cl.modules.AbstractModule;
import uniolunisaar.adam.tools.Logger;
import uniolunisaar.adam.logic.ui.cl.modules.Modules;

/**
 *
 * @author Manuel Gieseking
 */
public class AdamUI {

    public static boolean debug = false;
    public static final int CONSOLE_WIDTH = HelpFormatter.DEFAULT_WIDTH + 20;
//    public static final int CONSOLE_WIDTH = HelpFormatter.DEFAULT_WIDTH; // for having it exported to the pdf

    public static void main(String[] args, Modules modules, boolean debug) {
        // No arguments -> print available modules
        if (args.length == 0) {
            modules.printPossibleModules(new PrintWriter(System.out));
            return;
        }

        // find module
        for (AbstractModule module : modules.getModules()) {
            if (module.getName().equals(args[0])) {
                // if no further options are set -> print help dialog
                if (args.length == 1) {
                    module.printHelp();
                    return;
                }

                // create the command line parser
                CommandLineParser parser = new BasicParser();
                // delete the first argument of the argument list
                args = Arrays.copyOfRange(args, 1, args.length);
                try {
                    // get the Options
                    Options options = module.getOptions();
                    // parse the command line arguments
                    CommandLine line = parser.parse(options, args);
                    // if the help flag is set -> print help dialog
                    if (line.hasOption("help")) {
                        module.printHelp();
                        return;
                    }
                    // try to execute the module
                    module.execute(line);
                    return;
                } catch (ParseException exp) {
                    if (!exp.getMessage().equals("alreadyHandled")) {
                        Logger.getInstance().addError("Invalid use of '" + module.getName() + "': " + exp.getMessage(), exp);
                        module.printHelp();
                    }
                    return;
                } catch (uniol.apt.io.parser.ParseException exp) {
                    String msg = exp.getMessage();
                    if (exp.getCause() != null) {
                        msg += ": " + exp.getCause().getMessage();
                    }
                    Logger.getInstance().addError(msg, exp);
                    return;
                } catch (Exception exp) {
                    if (exp.getMessage() != null && !debug) {
                        Logger.getInstance().addError(exp.getMessage(), exp);
                    } else {
                        exp.printStackTrace();
                    }
                    return;
                }
            }
        }
        Logger.getInstance().addError("'" + args[0] + "' is not a suitable module.", new Exception());
        modules.printPossibleModules(new PrintWriter(System.out));
    }
}
