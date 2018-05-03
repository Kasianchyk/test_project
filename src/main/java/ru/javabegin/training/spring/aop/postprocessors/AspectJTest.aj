package ru.javabegin.training.spring.aop.postprocessors;

import com.sun.istack.internal.logging.Logger;

public aspect AspectJTest {
    private Logger logger = Logger.getLogger(this.getClass());

    pointcut traced() : execution();
}
