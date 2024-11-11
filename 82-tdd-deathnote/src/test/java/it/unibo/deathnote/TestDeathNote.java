package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.lang.Thread.sleep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImplementation;

class TestDeathNote {

    private DeathNote deathNote;
    private static final String JOHN_DOE = "John Doe";
    private static final String GINA_DOE = "Gina Doe";
    private static final int DETAILS_FAIL_TIME = 40;
    private static final int CAUSE_FAIL_TIME = 100;

    @BeforeEach
    public void setUp() {
        deathNote = new DeathNoteImplementation();
    }

    @Test
    public void testIllegalArguments() {
        assertThrows(IllegalArgumentException.class, () -> {deathNote.getRule(-1);});
        assertThrows(IllegalArgumentException.class, () -> {deathNote.getRule(0); });
        assertThrows(IllegalArgumentException.class, () -> {deathNote.getRule(DeathNote.RULES.size() + 1);});
    }

    public void testRulesContent() {
        for(int i = 1; i <= DeathNote.RULES.size(); i++) {
            final var rule = deathNote.getRule(i);
            assertNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    public void testHumanDeath() {
        assertFalse(deathNote.isNameWritten(JOHN_DOE));
        deathNote.writeName(JOHN_DOE);
        assertTrue(deathNote.isNameWritten(JOHN_DOE));
        
        assertFalse(deathNote.isNameWritten(GINA_DOE));
        assertFalse(deathNote.isNameWritten(""));
    }

    public void testDeathCause() throws InterruptedException {
        assertThrows(IllegalStateException.class, () -> { deathNote.writeDeathCause("cancer"); });
        
        deathNote.writeName(JOHN_DOE);
        assertEquals("heart attack", deathNote.getDeathCause(JOHN_DOE));

        deathNote.writeName(GINA_DOE);
        assertTrue(deathNote.writeDeathCause("karting accident"));
        assertEquals("karting accident", deathNote.getDeathCause(GINA_DOE));
        
        sleep(CAUSE_FAIL_TIME);
        
        assertFalse(deathNote.writeDeathCause("stabbed on the streets of nyc"));
        assertEquals("karting accident", deathNote.getDeathCause(GINA_DOE));
    }

    public void testDeathDetails() throws InterruptedException {
        assertThrows(IllegalStateException.class, () -> { deathNote.writeDetails("stage 4 cancer across all organs"); });
        
        deathNote.writeName(JOHN_DOE);
        assertTrue(deathNote.getDeathDetails(JOHN_DOE).isEmpty());
        assertTrue(deathNote.writeDetails("ran for too long"));
        assertEquals("ran for too long", deathNote.getDeathDetails(JOHN_DOE));

        deathNote.writeName(GINA_DOE);
        sleep(DETAILS_FAIL_TIME);
        assertFalse(deathNote.writeDetails("was intoxicated by mushrooms"));
        assertTrue(deathNote.getDeathDetails(JOHN_DOE).isEmpty());
    }
}