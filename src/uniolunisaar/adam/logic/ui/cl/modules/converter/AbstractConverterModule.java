package uniolunisaar.adam.logic.ui.cl.modules.converter;

import java.util.Map;
import org.apache.commons.cli.Option;
import uniolunisaar.adam.ds.ui.cl.parameters.IOParameters;
import uniolunisaar.adam.logic.ui.cl.modules.AbstractSimpleModule;

/**
 *
 * @author Manuel Gieseking
 */
public abstract class AbstractConverterModule extends AbstractSimpleModule {

    @Override
    public Map<String, Option> createOptions() {
        Map<String, Option> options = super.createOptions();
        // Add IO
        options.putAll(IOParameters.createOptions());
        return options;
    }
}
