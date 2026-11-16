package team.projectpulse.section;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.projectpulse.AbstractIntegrationTest;
import team.projectpulse.course.CourseRepository;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Integration tests for SectionSpecs")
class SectionSpecsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    CourseRepository courseRepository;

    private Locale originalDefault;

    @BeforeEach
    void rememberLocale() {
        this.originalDefault = Locale.getDefault();
    }

    @AfterEach
    void restoreLocale() {
        Locale.setDefault(this.originalDefault);
    }

    @Test
    @DisplayName("containsSectionName matches case-insensitively under a Turkish default locale")
    void testContainsSectionNameUnderTurkishLocale() {
        Section section = new Section();
        section.setSectionName("Intro to Software");
        section.setCourse(this.courseRepository.findAll().get(0));
        Section saved = this.sectionRepository.save(section);

        // In Turkish, "I".toLowerCase() is dotless "ı", so a locale-sensitive lower-casing of "INTRO" gives "ıntro".
        Locale.setDefault(Locale.forLanguageTag("tr"));

        List<Section> found = this.sectionRepository.findAll(SectionSpecs.containsSectionName("INTRO"));

        assertThat(found).extracting(Section::getSectionId).contains(saved.getSectionId());
    }
}
