package org.ea.constant;
/**
 * <p>Defines constant message strings used for logging or console output during STL file processing and geometric operations.</p>
 *
 * @precondition None – this interface is intended only for exposing constant strings.
 * @postcondition All messages are statically accessible and immutable.
 */
public interface Numbers {
    int HEADER_LENGTH = 80;
    int TRIANGLE_AMOUNT_LENGTH = 4;
    int FILE_START = 0;
    int NEXT = 1;
    int NEXT_TWO = 2;
    int NEXT_THREE = 3;
    int EULER_RESULT = 2;
    double HIGHLIGHT_VALUE = 0.25;
    double SENSITIVITY = 0.09;
    double ROTATION_VALUE = 0.2;
    double ANGLE = 90;
    double OBJ_SIZE_MULTIPLICATOR = 1.20;
    double OBJ_MAX_SIZE = 50;
    double THICKNESS_MULTIPLICATOR = 0.02;
    double MAJOR_MULTIPLICATOR = 0.7;

}
