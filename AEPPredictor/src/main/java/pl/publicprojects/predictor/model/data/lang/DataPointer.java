package pl.publicprojects.predictor.model.data.lang;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.publicprojects.predictor.model.data.container.VirtualDataLineContainer;


/**
 * Container that point for specific VirtualDataLineContainer
 * Every VirtualVariable should have the same DataPointer with reference the same data column
 */
@Getter
@Setter
@NoArgsConstructor
public class DataPointer {
    private VirtualDataLineContainer pointerContainer;
}
