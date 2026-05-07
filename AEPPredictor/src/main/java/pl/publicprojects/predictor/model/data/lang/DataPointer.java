package pl.publicprojects.predictor.model.data.lang;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.publicprojects.predictor.model.data.container.VirtualDataLineContainer;

@Getter
@Setter
@NoArgsConstructor
public class DataPointer {
    private VirtualDataLineContainer pointerContainer;
}
