package org.ea.utiltities;

import org.ea.constant.Arguments;
import org.ea.constant.GeometricConstants;
import org.ea.constant.Messages;
import org.ea.exceptions.EndOfFileReachedException;
import org.ea.exceptions.OffsetOutOfRangeException;
import org.ea.exceptions.STLReaderException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Selects appropriate STLReader implementation based on file content.
 */
public class STLFileReaderSelector {

    /**
     * Selects an STLReader instance for the given file, with optional queue.
     *
     * @param file              the STL file to read
     * @param triangleDataQueue optional queue for passing triangle data
     * @return STLReader instance (ASCII or Byte), or exits on error
     */
    public STLReader selectReader(File file, BlockingQueue<List<Float>> triangleDataQueue) {
        try {
            STLReader testReader = new STLAsciiReader(file); // Zum Header prüfen
            boolean isAscii = testReader.readHeader().contains(GeometricConstants.SOLID);

            Logger.info(isAscii ? Messages.STARTED_READING_STL_ASCII : Messages.STARTED_READING_STL_BYTE);

            if (isAscii) {
                return (triangleDataQueue != null)
                        ? new STLAsciiReader(file, triangleDataQueue)
                        : new STLAsciiReader(file);
            } else {
                return (triangleDataQueue != null)
                        ? new STLByteReader(file, triangleDataQueue)
                        : new STLByteReader(file);
            }
        } catch (STLReaderException | IOException e) {
            Logger.error(e.getMessage());
            System.exit(Arguments.EXIT_ERROR);
        }
        return null;
    }

    /**
     * Convenience overload: selects reader without a queue.
     */
    public STLReader selectReader(File file) {
        return selectReader(file, null);
    }

}