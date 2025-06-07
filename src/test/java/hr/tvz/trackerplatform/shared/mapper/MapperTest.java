package hr.tvz.trackerplatform.shared.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    private Mapper mapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        mapper = new Mapper(modelMapper);
    }

    @Test
    void map_shouldMapSingleEntity() {
        SourceClass source = new SourceClass("John", 30);

        DestinationClass destination = mapper.map(source, DestinationClass.class);

        assertThat(destination).isNotNull();
        assertThat(destination.getName()).isEqualTo("John");
        assertThat(destination.getAge()).isEqualTo(30);
    }

    @Test
    void mapList_shouldMapCollectionOfEntities() {
        List<SourceClass> sources = List.of(
                new SourceClass("John", 30),
                new SourceClass("Jane", 25),
                new SourceClass("Bob", 40)
        );

        List<DestinationClass> destinations = mapper.mapList(sources, DestinationClass.class);

        assertThat(destinations).hasSize(3);
        assertThat(destinations.get(0).getName()).isEqualTo("John");
        assertThat(destinations.get(0).getAge()).isEqualTo(30);
        assertThat(destinations.get(1).getName()).isEqualTo("Jane");
        assertThat(destinations.get(1).getAge()).isEqualTo(25);
        assertThat(destinations.get(2).getName()).isEqualTo("Bob");
        assertThat(destinations.get(2).getAge()).isEqualTo(40);
    }

    @Test
    void mapList_shouldReturnEmptyList_whenSourceIsEmpty() {
        List<SourceClass> sources = List.of();

        List<DestinationClass> destinations = mapper.mapList(sources, DestinationClass.class);

        assertThat(destinations).isEmpty();
    }

    static class SourceClass {
        private String name;
        private int age;

        public SourceClass() {
        }

        public SourceClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SourceClass that = (SourceClass) o;
            return age == that.age && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    static class DestinationClass {
        private String name;
        private int age;

        public DestinationClass() {
        }

        public DestinationClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DestinationClass that = (DestinationClass) o;
            return age == that.age && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }
}
