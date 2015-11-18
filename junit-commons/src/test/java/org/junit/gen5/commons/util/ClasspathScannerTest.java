/*
 * Copyright 2015 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.gen5.commons.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class ClasspathScannerTest {

	private ClasspathScanner classpathScanner;

	@Before
	public void init() {
		classpathScanner = new ClasspathScanner(ReflectionUtils::getDefaultClassLoader, ReflectionUtils::loadClass);
	}

	@Test
	public void findAllClassesInThisPackage() throws IOException, ClassNotFoundException {
		List<Class<?>> classes = Arrays.asList(classpathScanner.scanForClassesInPackage("org.junit.gen5.commons"));
		Assert.assertTrue("Should be at least 20 classes", classes.size() >= 20);
		Assert.assertTrue(classes.contains(NestedClassToBeFound.class));
		Assert.assertTrue(classes.contains(MemberClassToBeFound.class));
	}

	@Test
	public void isPackage() throws IOException, ClassNotFoundException {
		Assert.assertTrue(classpathScanner.isPackage("org.junit.gen5.commons"));
		Assert.assertFalse(classpathScanner.isPackage("org.doesnotexist"));

	}

	@Test
	public void findAllClassesInRoot() throws IOException, ClassNotFoundException {
		File root = getTestClasspathRoot();
		List<Class<?>> classes = Arrays.asList(classpathScanner.scanForClassesInClasspathRoot(root));

		Assert.assertTrue("Should be at least 20 classes", classes.size() >= 20);
		Assert.assertTrue(classes.contains(ClasspathScannerTest.class));
	}

	private File getTestClasspathRoot() {
		String fullClassPath = System.getProperty("java.class.path");
		final String separator = System.getProperty("path.separator");
		Optional<String> testRoot = Arrays.stream(fullClassPath.split(separator)).filter(
			s -> s.endsWith("/classes/test")).findFirst();

		Assume.assumeTrue("There should be a test root", testRoot.isPresent());

		return new File(testRoot.get());
	}

	class MemberClassToBeFound {
	}

	static class NestedClassToBeFound {
	}

}
