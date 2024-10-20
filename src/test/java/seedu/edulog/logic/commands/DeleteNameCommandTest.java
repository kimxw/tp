package seedu.edulog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.edulog.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.edulog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.edulog.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.edulog.testutil.TypicalEdulog.getTypicalEduLog;
import static seedu.edulog.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.edulog.testutil.TypicalNames.NAME_FIRST_STUDENT;
import static seedu.edulog.testutil.TypicalNames.NAME_SECOND_STUDENT;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.edulog.logic.Messages;
import seedu.edulog.model.Model;
import seedu.edulog.model.ModelManager;
import seedu.edulog.model.UserPrefs;
import seedu.edulog.model.student.Name;
import seedu.edulog.model.student.Student;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteNameCommandTest {

    private Model model = new ModelManager(getTypicalEduLog(), new UserPrefs());

    @Test
    public void execute_validNameUnfilteredList_success() {
        Student studentToDelete = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        DeleteCommand deleteCommand = new DeleteNameCommand(NAME_FIRST_STUDENT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_STUDENT_SUCCESS,
                Messages.format(studentToDelete));

        ModelManager expectedModel = new ModelManager(model.getEduLog(), new UserPrefs());
        expectedModel.deleteStudent(studentToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidNameUnfilteredList_throwsCommandException() {
        Name invalidName = new Name("Invalid");
        DeleteCommand deleteCommand = new DeleteNameCommand(invalidName);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_NAME);
    }

    @Test
    public void execute_validNameFilteredList_success() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Student studentToDelete = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        DeleteCommand deleteCommand = new DeleteNameCommand(NAME_FIRST_STUDENT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_STUDENT_SUCCESS,
                Messages.format(studentToDelete));

        Model expectedModel = new ModelManager(model.getEduLog(), new UserPrefs());
        expectedModel.deleteStudent(studentToDelete);
        showNoStudent(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidNameFilteredList_throwsCommandException() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);
        List<Name> names = model.getEduLog().getStudentList().stream().map(Student::getName).toList();

        Name unFilteredStudent = NAME_SECOND_STUDENT;
        // ensures that unFilteredStudent is still in bounds of edulog book list
        assertTrue(names.contains(unFilteredStudent));

        DeleteCommand deleteCommand = new DeleteNameCommand(unFilteredStudent);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_NAME);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteNameCommand(NAME_FIRST_STUDENT);
        DeleteCommand deleteSecondCommand = new DeleteNameCommand(NAME_SECOND_STUDENT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteNameCommand(NAME_FIRST_STUDENT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different student -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Name targetName = new Name("targetName");
        DeleteNameCommand deleteCommand = new DeleteNameCommand(targetName);
        String expected = DeleteNameCommand.class.getCanonicalName() + "{targetName=" + targetName + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoStudent(Model model) {
        model.updateFilteredStudentList(p -> false);

        assertTrue(model.getFilteredStudentList().isEmpty());
    }
}
