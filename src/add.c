#include "src/add.h"

#include <stdio.h>

struct add_result add(int x, int y)
{
    struct add_result res = { .result = x + y };
    return res;
}

int sub(int x, int y)
{
    return x - y;
}
