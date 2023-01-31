package com.ekinox.utils

def fNot[A](f: A => Boolean): A => Boolean = (a: A) => !f(a)
