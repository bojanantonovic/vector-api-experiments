module vector.api.experiments {

	requires jdk.incubator.vector;
	requires org.junit.jupiter.api;

	opens experiments to org.junit.platform.commons;
}
