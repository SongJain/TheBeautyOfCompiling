// BianYiCode.cpp : 此文件包含 "main" 函数。程序执行将在此处开始并结束。
//
#include "MyTokenReader.h"
#include <iostream>
#include<string>
using namespace std;
int main() {
	string str = "age >= 45";
	string str1 = "int age = 45";
	MyTokenReader TR(str1);
	TR.Print();
}