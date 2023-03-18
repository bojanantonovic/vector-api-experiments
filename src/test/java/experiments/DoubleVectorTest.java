package experiments;

import jdk.incubator.vector.*;
import org.junit.jupiter.api.Test;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DoubleVectorTest {

	private static final double[] ARRAY = IntStream.rangeClosed(1, 8).mapToDouble(x -> x).toArray();
	private static final Vector<Double> VECTOR = DoubleVector.SPECIES_512.fromArray(ARRAY, 0);
	private static final Vector<Double> ONE = DoubleVector.SPECIES_512.broadcast(1);
	private static final Vector<Double> TWO = DoubleVector.SPECIES_512.broadcast(2);
	private static final Vector<Double> FOUR = DoubleVector.SPECIES_512.broadcast(4);

	private static final Vector<Double> TEN = DoubleVector.SPECIES_512.broadcast(10);

	@Test
	void fromArray_indexMap1() {
		// act
		final var vector = DoubleVector.fromArray(DoubleVector.SPECIES_512, //
				IntStream.rangeClosed(1, 100).mapToDouble(x -> 3 * x).toArray(), //
				0, new int[] {0, 1, 2, 3, 4, 5, 6, 7}, 0);
		// assert
		assertArrayEquals(new double[] {3, 6, 9, 12, 15, 18, 21, 24}, asArray(vector));
	}

	@Test
	void fromArray_indexMap2() {
		// act
		final var vector = DoubleVector.fromArray(DoubleVector.SPECIES_512, //
				IntStream.rangeClosed(1, 100).mapToDouble(x -> 3 * x).toArray(), //
				0, new int[] {10, 20, 30, 40, 50, 60, 70, 80}, 0);
		// assert
		assertArrayEquals(new double[] {33, 63, 93, 123, 153, 183, 213, 243}, asArray(vector));
	}

	@Test
	void add() {
		// act
		final var result = VECTOR.add(ONE);
		// assert
		assertArrayEquals(DoubleStream.of(ARRAY) //
				.map(x -> x + 1) //
				.toArray(), asArray(result));
	}

	@Test
	void sub() {
		// act
		final var result = VECTOR.sub(ONE);
		// assert
		assertArrayEquals(DoubleStream.of(ARRAY) //
				.map(x -> x - 1) //
				.toArray(), asArray(result));
	}

	@Test
	void mul() {
		// act
		final var result = VECTOR.mul(TWO);
		// assert
		assertArrayEquals(DoubleStream.of(ARRAY) //
				.map(x -> x * 2) //
				.toArray(), asArray(result));
	}

	@Test
	void div() {
		// act
		final var result = VECTOR.div(TWO);
		// assert
		assertArrayEquals(DoubleStream.of(ARRAY) //
				.map(x -> x / 2) //
				.toArray(), asArray(result));
	}

	@Test
	void sqrt() {
		// act
		final var result = VECTOR.lanewise(VectorOperators.SQRT);
		// assert
		assertArrayEquals(DoubleStream.of(ARRAY) //
				.map(Math::sqrt) //
				.toArray(), asArray(result));
	}

	@Test
	void sin() {
		// act
		final var result = VECTOR.lanewise(VectorOperators.SIN);
		// assert
		assertArrayEquals(DoubleStream.of(ARRAY) //
				.map(Math::sin) //
				.toArray(), asArray(result));
	}

	@Test
	void eq() {
		// act
		final var mask = VECTOR.eq(FOUR);
		// assert
		assertArrayEquals(new boolean[] {false, false, false, true, false, false, false, false}, mask.toArray());
	}

	@Test
	void lessThan() {
		// act
		final var mask = VECTOR.lt(FOUR);
		// assert
		assertArrayEquals(new boolean[] {true, true, true, false, false, false, false, false}, mask.toArray());
	}

	@Test
	void addIfLessThan() {
		// act
		final var result = VECTOR.add(TEN, VECTOR.lt(FOUR));
		final var array = asArray(result);
		// assert
		assertArrayEquals(new double[] {11, 12, 13, 4, 5, 6, 7, 8}, array);
	}

	@Test
	void blendIfLessThan() {
		// act
		final var result = VECTOR.blend(TEN, VECTOR.lt(FOUR));
		final var array = asArray(result);
		// assert
		assertArrayEquals(new double[] {10, 10, 10, 4, 5, 6, 7, 8}, array);
	}

	private static double[] asArray(Vector<Double> result) {
		return (double[]) result.toArray();
	}
}
