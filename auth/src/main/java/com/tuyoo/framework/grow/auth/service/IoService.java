package com.tuyoo.framework.grow.auth.service;

public interface IoService
{
    String io0();

    String io500() throws InterruptedException;

    String io200() throws InterruptedException;

    String io1000() throws InterruptedException;

    String io3000() throws InterruptedException;

    String io5000() throws InterruptedException;
}
