package org.ea.utiltities;

import org.ea.constant.Arguments;
import org.ea.constant.GeometricConstants;
import org.ea.constant.Messages;
import org.ea.exceptions.EndOfFileReachedException;
import org.ea.exceptions.OffsetOutOfRangeException;
import org.ea.exceptions.STLReaderException;

import java.io.File;
import java.io.IOException;

/**
 * Selects appropriate STLReader implementation based on file content.
 */
public class STLFileReaderSelector {
    /**
     * Selects an STLReader instance for the given file.
     *
     * @param file the STL file to read
     * @return STLReader instance (ASCII or Byte reader)
     *
     * @precondition file != null && file.exists() && file.isFile()
     * @postcondition returns a valid STLReader or exits on error
     */
    public static STLReader selectReader(File file) {
        try {
            STLReader stlAsciiReader = new STLAsciiReader(file);
            if (stlAsciiReader.readHeader().contains(GeometricConstants.SOLID)) {
                Logger.info(Messages.STARTED_READING_STL_ASCII);
                return stlAsciiReader;
            } else {
                Logger.info(Messages.STARTED_READING_STL_BYTE);
                return new STLByteReader(file);
            }
        } catch (STLReaderException | IOException e) {
            Logger.error(e.getMessage());
            System.exit(Arguments.EXIT_ERROR);
        }
        return null;
    }
}
