package org.ea.utiltities;

import org.ea.constant.FileExtensions;
import org.ea.exceptions.EndOfFileReachedException;
import org.ea.exceptions.OffsetOutOfRangeException;

import java.io.IOException;
import java.util.List;

/**
 * Represents an abstract STL file reader interface.
 * Implementations can read STL files in different formats (e.g., ASCII, binary).
 *
 * @precondition File input stream must be valid and readable.
 * @postcondition Header and triangle data can be extracted or transformed.
 */
public interface STLReader extends Runnable {

    /**
     * Reads the header section of the STL file.
     *
     * @param <T> the type of elements returned (depends on implementation)
     * @return a list of header elements
     * @throws IOException if an I/O error occurs
     * @throws EndOfFileReachedException if the end of file is reached prematurely
     * @throws OffsetOutOfRangeException if attempting to read beyond bounds
     * @precondition Input file stream must be open and positioned at the beginning
     * @postcondition A list of parsed header content is returned
     */
    <T> List<T> readHeader() throws IOException, EndOfFileReachedException, OffsetOutOfRangeException;

    /**
     * Reads the triangle data from the STL file.
     *
     * @param <T> the type of elements returned (depends on implementation)
     * @return a list of triangle data
     * @throws IOException if an I/O error occurs
     * @throws EndOfFileReachedException if the end of file is reached prematurely
     * @throws OffsetOutOfRangeException if attempting to read beyond bounds
     * @precondition File header must be read before this call
     * @postcondition A list of triangle data is returned for processing
     */
    <T> List<T> readTriangleData() throws IOException, EndOfFileReachedException, OffsetOutOfRangeException;

    /**
     * Checks if the given file name does not end with the STL extension.
     *
     * @param fileName the name of the file to check
     * @return true if file does not end with .stl, false otherwise
     * @precondition fileName must not be null
     * @postcondition Boolean result indicates STL file validity by extension
     */
    default boolean isNotSTLFile(String fileName) {
        return !fileName.endsWith(FileExtensions.STL);
    }
}
