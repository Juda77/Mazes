package priorityqueues;

import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Some provided tests for ArrayHeapMinPQ.
 *
 * If you wish, you can extend this class and override createMinPQ and assertThat to run on
 * NaiveMinPQ instead (although you'd need to make a dummy NaiveMinPQAssert class that with an
 * invariant check that does nothing).
 */
public class ArrayHeapMinPQTests extends BaseTest {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new ArrayHeapMinPQ<>();
    }

    protected <T> AbstractHeapMinPQAssert<T> assertThat(ExtrinsicMinPQ<T> pq) {
        return new ArrayHeapMinPQAssert<>((ArrayHeapMinPQ<T>) pq);
    }

    @Nested
    @DisplayName("New Empty")
    class NewEmpty {
        ExtrinsicMinPQ<String> setUpMinPQ() {
            return createMinPQ();
        }

        @Test
        void size_returns0() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            int output = pq.size();
            assertThat(output).isEqualTo(0);
            assertThat(pq).isValid();
        }

        @Test
        void contains_returnsFalse() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            boolean output = pq.contains("Winthrop");
            assertThat(output).isFalse();
            assertThat(pq).isValid();
        }

        @Test
        void peekMin_throwsNoSuchElement() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThatThrownBy(pq::peekMin).isInstanceOf(NoSuchElementException.class);
            assertThat(pq).isValid();
        }

        @Test
        void removeMin_throwsNoSuchElement() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThatThrownBy(pq::removeMin).isInstanceOf(NoSuchElementException.class);
            assertThat(pq).isValid();
        }

        @Test
        void changePriority_throwsNoSuchElement() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThatThrownBy(() -> pq.changePriority("Harold Hill", 7)).isInstanceOf(NoSuchElementException.class);
            assertThat(pq).isValid();
        }

        @Test
        void add_nullItem_doesntThrowIllegalArgument() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            pq.add(null, 15);
            assertThat(pq).isValid();
        }
    }

    @Nested
    @DisplayName("Empty After Adding/Removing 3")
    class EmptyAfterAddRemove extends NewEmpty {
        @Override
        ExtrinsicMinPQ<String> setUpMinPQ() {
            ExtrinsicMinPQ<String> pq = createMinPQ();
            pq.add("Morticia", 1);
            pq.add("Gomez", 2);
            pq.add("Wednesday", 3);
            pq.removeMin();
            pq.removeMin();
            pq.removeMin();
            return pq;
        }
    }

    @Nested
    @DisplayName("Add 3 Increasing Priority")
    class Add3Increasing {
        String min = "Ariel";

        ExtrinsicMinPQ<String> setUpMinPQ() {
            ExtrinsicMinPQ<String> pq = createMinPQ();
            pq.add("Ariel", 1);
            pq.add("Eric", 2);
            pq.add("Sebastian", 3);
            return pq;
        }

        @Test
        void isValid() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThat(pq).isValid();
        }

        @Test
        void size_returns3() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            int output = pq.size();
            assertThat(output).isEqualTo(3);
            assertThat(pq).isValid();
        }

        @Test
        void contains_withContainedItem_returnsTrue() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            boolean output = pq.contains(this.min);
            assertThat(output).isTrue();
            assertThat(pq).isValid();
        }

        @Test
        void contains_withNotContainedItem_returnsFalse() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            boolean output = pq.contains("Triton");
            assertThat(output).isFalse();
            assertThat(pq).isValid();
        }

        @Test
        void peekMin_returnsCorrectItem() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            String output = pq.peekMin();
            assertThat(output).isEqualTo(this.min);
            assertThat(pq).isValid();
        }

        @Test
        void removeMin_returnsCorrectItem() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            String output = pq.removeMin();
            assertThat(output).isEqualTo(this.min);
            assertThat(pq).isValid();
        }

        @Test
        void add_nullItem_doesntThrowIllegalArgument() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            pq.add(null, 15);
            assertThat(pq).isValid();
        }

        @Test
        void add_duplicateItem_throwsIllegalArgument() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThatThrownBy(() -> pq.add(this.min, 15)).isInstanceOf(IllegalArgumentException.class);
            assertThat(pq).isValid();
        }
    }

    @Nested
    @DisplayName("Add 3 Decreasing Priority")
    class Add3Decreasing extends Add3Increasing {
        Add3Decreasing() {
            min = "June";
        }

        @Override
        ExtrinsicMinPQ<String> setUpMinPQ() {
            ExtrinsicMinPQ<String> pq = createMinPQ();
            pq.add("Rose", 3);
            pq.add("Louise", 2);
            pq.add("June", 1);
            return pq;
        }
    }

    @Nested
    @DisplayName("Add 3 Arbitrary Priority")
    class Add3Arbitrary extends Add3Increasing {
        Add3Arbitrary() {
            min = "Benny";
        }

        @Override
        ExtrinsicMinPQ<String> setUpMinPQ() {
            ExtrinsicMinPQ<String> pq = createMinPQ();
            pq.add("Usnavi", 3);
            pq.add("Benny", 1);
            pq.add("Nina", 2);
            return pq;
        }
    }

    @Nested
    @DisplayName("Add 3 Same Priority")
    class Add3Same {
        ExtrinsicMinPQ<String> setUpMinPQ() {
            ExtrinsicMinPQ<String> pq = createMinPQ();
            pq.add("Seymour", 7);
            pq.add("Audrey", 7);
            pq.add("Mushnik", 7);
            return pq;
        }

        @Test
        void isValid() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThat(pq).isValid();
        }

        @Test
        void size_returns3() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            int output = pq.size();
            assertThat(output).isEqualTo(3);
            assertThat(pq).isValid();
        }

        @Test
        void contains_withContainedItem_returnsTrue() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            boolean output = pq.contains("Seymour");
            assertThat(output).isTrue();
            assertThat(pq).isValid();
        }

        @Test
        void removeMinRepeatedly_returnsAllItems() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThat(pq).as("invariant check before removing all elements").isValid();
            List<String> output = removeAll(pq);
            assertThat(output).containsExactlyInAnyOrder("Seymour", "Audrey", "Mushnik");
            assertThat(pq).isValid();
        }
    }

    @Nested
    @DisplayName("Add 10 Increasing Priority")
    class Add10Increasing {
        String[] correctOrdering = {"Jean Valjean", "Javert", "Cosette", "Marius", "Eponine", "Gavroche", "Fantine",
                                    "Thenardier", "Enjolras", "Grantaire"};

        ExtrinsicMinPQ<String> setUpMinPQ() {
            ExtrinsicMinPQ<String> pq = createMinPQ();
            pq.add("Jean Valjean", 1);
            pq.add("Javert", 2);
            pq.add("Cosette", 3);
            pq.add("Marius", 4);
            pq.add("Eponine", 5);
            pq.add("Gavroche", 6);
            pq.add("Fantine", 7);
            pq.add("Thenardier", 8);
            pq.add("Enjolras", 9);
            pq.add("Grantaire", 10);

            return pq;
        }

        @Test
        void isValid() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThat(pq).isValid();
        }

        @Test
        void size_returns10() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            int output = pq.size();
            assertThat(output).isEqualTo(10);
            assertThat(pq).isValid();
        }

        @Test
        void removeMinRepeatedly_returnsItemsInCorrectOrder() {
            ExtrinsicMinPQ<String> pq = setUpMinPQ();
            assertThat(pq).as("invariant check before removing all elements").isValid();
            List<String> output = removeAll(pq);
            assertThat(output).containsExactly(correctOrdering);
            assertThat(pq).isValid();
        }
    }

    @Nested
    @DisplayName("Add 10 Arbitrary Priority")
    class Add10Arbitrary extends Add10Increasing {
        Add10Arbitrary() {
            this.correctOrdering = new String[]{"Eponine", "Thenardier", "Jean Valjean", "Marius", "Fantine",
                                                "Enjolras", "Cosette", "Gavroche", "Javert", "Grantaire"};
        }

        @Override
        ExtrinsicMinPQ<String> setUpMinPQ() {
            ExtrinsicMinPQ<String> pq = createMinPQ();
            pq.add("Jean Valjean", 3);
            pq.add("Javert", 9);
            pq.add("Cosette", 7);
            pq.add("Marius", 4);
            pq.add("Eponine", 1);
            pq.add("Gavroche", 8);
            pq.add("Fantine", 5);
            pq.add("Thenardier", 2);
            pq.add("Enjolras", 6);
            pq.add("Grantaire", 10);
            return pq;
        }
    }

    @Nested
    @DisplayName("Misc.")
    class Miscellaneous {
        @Test
        void usingMultipleHeapsSimultaneously_doesNotCauseInterference() {
            ExtrinsicMinPQ<String> pq1 = createMinPQ();
            ExtrinsicMinPQ<String> pq2 = createMinPQ();

            pq1.add("Oliver", 1);
            pq2.add("Fagin", 2);
            pq1.add("Bill Sikes", 3);

            assertThat(pq1.size()).isEqualTo(2);
            assertThat(pq2.size()).isEqualTo(1);
        }

        @Test
        void addingNull_containsKeyNull() {
            ExtrinsicMinPQ<String> pq = createMinPQ();

            pq.add(null, 15);
            assertThat(pq.contains(null)).isTrue();
            assertThat(pq).isValid();
        }

        @Test
        void addingWithNegativePriorities_doesNotFail() {
            ExtrinsicMinPQ<String> pq = createMinPQ();

            pq.add("Nancy", -10);
            assertThat(pq.peekMin()).isEqualTo("Nancy");
            assertThat(pq).isValid();
        }
    }

    /**
     * Removes all items from given priority queue, and returns them in the order removed.
     *
     * This is not a "unit" that's great for unit testing since it involves calling too many
     * operations; it sacrifices some ease of debugging in favor of test brevity and thoroughness.
     */
    protected <T> List<T> removeAll(ExtrinsicMinPQ<T> pq) {
        return IntStream.range(0, pq.size()).mapToObj(i -> pq.removeMin()).collect(Collectors.toList());
    }
}
