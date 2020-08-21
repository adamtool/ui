package uniolunisaar.adam.logic.ui.cl.modules.exporter;

import java.util.Collection;
import org.apache.commons.cli.Option;
import uniolunisaar.adam.logic.ui.cl.modules.AbstractModule;
import uniolunisaar.adam.logic.ui.cl.modules.Modules;

/**
 *
 * @author Manuel Gieseking
 */
public class BashCompletion {

    public static String getBashCompletionContent(Modules modules) {
        StringBuilder sb = new StringBuilder("# complete adam\n");
        sb.append("_adam()\n");
        sb.append("{\n");
        sb.append("local cur=${COMP_WORDS[COMP_CWORD]}\n");
        sb.append("local prev=${COMP_WORDS[COMP_CWORD-1]}\n");
        sb.append("\n");
        sb.append("if [[ \"$COMP_CWORD\" == 1 ]]; then \n");
        sb.append("COMPREPLY=( $( compgen -W \"");
        for (AbstractModule mod : modules.getModules()) {
            sb.append(mod.getName()).append(" ");
        }
        sb.append("\" -- $cur ) )\n");
        sb.append("fi\n\n");
        sb.append("# complete folder/files for input/output parameters\n");
        sb.append("if [[ \"$prev\" == -i || \"$prev\" == -o || \"$prev\" == -output || \"$prev\" == -input ]]; then\n");
        sb.append("return 0\n");
        sb.append("fi\n\n");
        sb.append("case ${COMP_WORDS[1]} in\n");
        for (AbstractModule mod : modules.getModules()) {
            String options = "";
            Collection<Option> opts = mod.getOptionsList();
            for (Option option : opts) {
                String opt = option.getLongOpt();
                options += "-" + opt == null ? option.getOpt() : opt + " ";
            }
            sb.append(mod.getName()).append(" )\n");
            sb.append("COMPREPLY=( $( compgen -W \"").append(options).append("\" -- $cur ) );;\n");
        }
        sb.append("esac\n");
        sb.append("}\n");
        sb.append("complete -o bashdefault -o default -F _adam adam\n");
        return sb.toString();
    }
}
