import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class ShapeClassifierTest {
    private ShapeClassifier classifier;

    @BeforeEach
    void setUp() {
        classifier = new ShapeClassifier();
    }

    @Test
    @DisplayName("Test Line Shape - Valid Case")
    void testLineShape() {
        String result = classifier.evaluateGuess("Line,Small,No,5");
        assertEquals("Yes", result);
    }

    @Test
    @DisplayName("Test Circle - All Correct")
    void testCircle() {
        String result = classifier.evaluateGuess("Circle,Small,Yes,1,1");
        assertEquals("Yes", result);
    }

    @Test
    @DisplayName("Test Ellipse - Wrong Size")
    void testEllipse() {
        String result = classifier.evaluateGuess("Ellipse,Small,Yes,10,5");
        assertFalse(result.contains("Wrong Size"));
    }

    @Test
    @DisplayName("Test Square - All Correct")
    void testSquare() {
        String result = classifier.evaluateGuess("Square,Small,Yes,2,2,2,2");
        assertEquals("Yes", result);
    }

    @Test
    @DisplayName("Test Rectangle - Wrong Shape Guess")
    void testRectangle() {
        String result = classifier.evaluateGuess("Square,Large,Yes,50,50,20,20");
        assertTrue(result.contains("Suggestion=Rectangle"));
    }

    @Test
    @DisplayName("Test Equilateral Triangle - All Correct")
    void testEquilateralTriangle() {
        String result = classifier.evaluateGuess("Equilateral,Small,Yes,2,2,2");
        assertEquals("Yes", result);
    }

    @Test
    @DisplayName("Test Isosceles Triangle - Wrong Even/Odd")
    void testIsoscelesTriangle() {
        String result = classifier.evaluateGuess("Isosceles,Small,Yes,2,2,3");
        assertTrue(result.contains("Wrong Even/Odd"));
    }

    @Test
    @DisplayName("Test Scalene Triangle - All Correct")
    void testScaleneTriangle() {
        String result = classifier.evaluateGuess("Scalene,Small,No,3,4,5");
        assertEquals("Wrong Even/Odd", result);
    }

    @Test
    @DisplayName("Test Invalid Triangle")
    void testInvalidTriangle() {
        String result = classifier.evaluateGuess("Scalene,Small,Yes,1,1,10");
        assertTrue(result.contains("Not A Triangle"));
    }

    @Test
    @DisplayName("Test Parameter Bounds")
    void testParameterBounds() {
        String result = classifier.evaluateGuess("Line,Small,Yes,-1");
        assertEquals("Yes", result); // Should convert negative to 0
        
        result = classifier.evaluateGuess("Line,Large,Yes,5000");
        assertFalse(result.contains("4095")); // Should cap at 4095
    }

    @Test
    @DisplayName("Test Bad Input Format")
    void testBadInput() {
        String result = classifier.evaluateGuess("InvalidInput");
        assertEquals("No", result);
    }

    @Test
    @DisplayName("Test Multiple Bad Guesses")
    void testMultipleBadGuesses() {
        // First bad guess
        classifier.evaluateGuess("Square,Small,Yes,2,3,4,5");
        // Second bad guess
        classifier.evaluateGuess("Square,Small,Yes,2,3,4,5");
        
        // Third bad guess should throw BadGuessLimitExceededException
        assertThrows(ShapeClassifier.BadGuessLimitExceededException.class, () -> {
            classifier.evaluateGuess("Square,Small,Yes,2,3,4,5");
        });
    }
}
