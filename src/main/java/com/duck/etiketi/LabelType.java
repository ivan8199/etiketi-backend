package com.duck.etiketi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabelType {
    private int width;
    private int height;
    private int rows;
    private int columns;
}
