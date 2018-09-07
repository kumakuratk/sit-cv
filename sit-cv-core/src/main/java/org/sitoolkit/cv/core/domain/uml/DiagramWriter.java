package org.sitoolkit.cv.core.domain.uml;

import org.sitoolkit.cv.core.domain.designdoc.Diagram;

public interface DiagramWriter<T extends DiagramModel> {

    Diagram write(T diagram);
}
