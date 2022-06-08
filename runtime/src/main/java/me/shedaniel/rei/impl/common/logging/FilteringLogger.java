/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.shedaniel.rei.impl.common.logging;

import me.shedaniel.rei.impl.common.InternalLogger;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FilteringLogger implements InternalLogger {
    private final InternalLogger logger;
    private final Level minLevel;
    
    public FilteringLogger(InternalLogger logger, Level minLevel) {
        this.logger = logger;
        this.minLevel = minLevel;
    }
    
    @Override
    public void throwException(Throwable throwable) {
        logger.throwException(throwable);
    }
    
    @Override
    public void log(Level level, String message) {
        if (level.isLessSpecificThan(minLevel))
            return;
        logger.log(level, message);
    }
    
    @Override
    public void log(Level level, String message, Throwable throwable) {
        if (level.isLessSpecificThan(minLevel))
            return;
        logger.log(level, message, throwable);
    }
}
