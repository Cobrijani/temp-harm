package com.github.cobrijani.tempharm.usecases;

import com.github.cobrijani.tempharm.DateRange;
import lombok.Builder;
import lombok.Data;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

@Data
@Builder
public class UseCase implements Arguments {

    private String name;

    private List<DateRange> inputs;

    private List<Tuple> output;

    @Override
    public Object[] get() {
        return new Object[] { name, inputs, output };
    }
}
