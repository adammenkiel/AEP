package pl.publicprojects.aep.tests.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValueContainer<T> {
    T value;
}
