package uniolunisaar.adam.logic.ui.cl.modules.converter.petrinet;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.io.parser.ParseException;
import uniolunisaar.adam.data.ui.cl.parameters.IOParameters;
import uniolunisaar.adam.exceptions.ui.cl.CommandLineParseException;
import uniolunisaar.adam.logic.ui.cl.modules.AbstractSimpleModule;
import uniolunisaar.adam.tools.Tools;
import uniolunisaar.adam.tools.Unfolder;
import uniolunisaar.adam.util.PNTools;

/**
 * TODO: add parameters (1) whether dot only, pdf only, or both 2) with the
 * original net or not
 *
 * @author Manuel Gieseking
 */
public class Pn2Unfolding extends AbstractSimpleModule {

    private static final String name = "pn2unfolding";
    private static final String descr = "Creates the unfolding of the given net for a maximal number of occuring transitions."
            + " Saves the unfolding and the original net to a pdf file by using Graphviz (dot has to be executable).";
    private static final String PARAMETER_NB_TRANSITIONS = "nb_tr";

    @Override
    public Map<String, Option> createOptions() {
        Map<String, Option> options = super.createOptions();
        // Add IO
        options.putAll(IOParameters.createOptions());
        // Add the nb transitions
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("numberOfTransitions");
        OptionBuilder.withDescription("The number of occuring transitions (>= 0).");
        OptionBuilder.isRequired();
        OptionBuilder.withLongOpt("nb_transitions");
        OptionBuilder.withType(Number.class);
        options.put(PARAMETER_NB_TRANSITIONS, OptionBuilder.create(PARAMETER_NB_TRANSITIONS));
        return options;
    }

    @Override
    public void execute(CommandLine line) throws IOException, ParseException, InterruptedException, CommandLineParseException, ClassNotFoundException, Exception {
        super.execute(line);
        PetriNet net = Tools.getPetriNet(IOParameters.getInput(line));
        PNTools.savePN2DotAndPDF(IOParameters.getOutput(line), net, false, false);
        int nb_transitions = ((Number) line.getParsedOptionValue(PARAMETER_NB_TRANSITIONS)).intValue();
        PetriNet unfolding = Unfolder.getInstance().unfold(net, nb_transitions);
        PNTools.savePN2DotAndPDF(IOParameters.getOutput(line) + "_unfolding", unfolding, true, true);
    }

    @Override
    public String getDescr() {
        return descr;
    }

    @Override
    public String getName() {
        return name;
    }
}
