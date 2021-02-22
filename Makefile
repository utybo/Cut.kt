SOURCES = src/add.c
OBJ = src/add.o
CFLAGS = -I.
TEST_SOURCES = $(wildcard tests/*.kt)

# Run the testsuite
check: kt_build/testsuite.kexe
	kt_build/testsuite.kexe

# Create the testsuite executable from the KLib and Kotlin source files
kt_build/testsuite.kexe: kt_build/test_target.klib kt_build/kotlin-native $(TEST_SOURCES)
	kt_build/kotlin-native/bin/kotlinc-native tests/*.kt -l kt_build/test_target.klib -o kt_build/testsuite.kexe

# Create a KLib from the static library. This generates everything Kotlin/Native needs.
kt_build/test_target.klib: kt_build/kotlin-native kt_build/test_target.a test_target.def
	kt_build/kotlin-native/bin/cinterop -def test_target.def -compiler-option -I${PWD} -o kt_build/test_target.klib

# Create a static library
kt_build/test_target.a: ${OBJ}
	mkdir -p kt_build
	ar rcs kt_build/test_target.a ${OBJ}

# Downloads Kotlin/Native
kt_build/kotlin-native:
	@echo "Downloading Kotlin/Native"
	@mkdir -p kt_build/kotlin-native
	@curl -L https://github.com/JetBrains/kotlin/releases/download/v1.4.30/kotlin-native-linux-1.4.30.tar.gz | tar -xz -C kt_build/kotlin-native --strip 1
