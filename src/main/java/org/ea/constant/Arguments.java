package org.ea.constant;


/**
 * <p>Collection of constants used for CLI argument handling and signaling
 * program termination status.</p>
 *
 * @precondition None – this interface is used solely to provide constants and should not be instantiated.
 * @postcondition All constants remain unchanged and available.
 */
public interface Arguments {

    int FILE_NAME_ARGUMENT = 0;
    int EXIT_ERROR = -1;
    int EXIT_SUCCESS = 0;

}