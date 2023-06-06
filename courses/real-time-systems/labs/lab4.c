#include "msp.h"
#include <stdio.h>
#include <stdlib.h>

int main()
{

    int which_led = 0;
    uint8_t last_three = 0x00;

    WDT_A->CTL = WDT_A_CTL_PW | WDT_A_CTL_HOLD; // Stop watchdog timer

    // set GPIO pins for P1 and P2
    P1SEL0 &= (uint8_t)(~((1 << 4) | (1 << 1) | (1 << 0)));
    P1SEL1 &= (uint8_t)(~((1 << 4) | (1 << 1) | (1 << 0)));

    P2SEL0 &= (uint8_t)(~((1 << 2) | (1 << 1) | (1 << 0)));
    P2SEL1 &= (uint8_t)(~((1 << 2) | (1 << 1) | (1 << 0)));

    // setting input pin
    P1DIR &= (uint8_t)(~((1 << 4) | (1 << 1)));

    // setting output pins for P1 and P2
    P1DIR |= (uint8_t)((1 << 0));
    P2DIR |= (uint8_t)(((1 << 2) | (1 << 1) | (1 << 0)));

    // pull down resistor
    P1OUT &= ~(uint8_t)(1 << 0);
    P2OUT &= ~(uint8_t)(((1 << 2) | (1 << 1) | (1 << 0)));

    // pull up resistor
    P1OUT |= (uint8_t)((1 << 4) | (1 << 1));
    // enabling resistor
    P1REN |= (uint8_t)((1 << 4) | (1 << 1));

    uint32_t i;
    while (1)
    {
        i = 100000;
        while ((P1IN & (uint8_t)(1 << 1)) && (P1IN & (uint8_t)(1 << 4)))
        {
        }
        while (i > 0)
        {
            i--;
        }

        // check if a button was pressed
        if ((!(P1IN & (uint8_t)(1 << 1))) || (!(P1IN & (uint8_t)(1 << 4))))
        {
        }

        if (!(P1IN & (uint8_t)(1 << 1)))
        {

            if (which_led)
            {
                which_led = 0;
            }
            else
            {
                which_led = 1;
            }
        }

        else
        {
            if ((!(P1IN & (uint8_t)(1 << 4))))
            {
                if (which_led)
                {
                    P1OUT ^= (uint8_t)(1 << 0); // RED LED
                }
                else
                {
                    if (last_three == 0x08)
                    {
                        last_three = 0x00;
                    }
                    P2OUT &= ~(uint8_t)(((1 << 2) | (1 << 1) | (1 << 0)));
                    P2OUT |= last_three;
                    last_three += 0x01;
                }
            }
            else
            {
                continue; // go back to beginning of loop
            }
        }
        while (!((P1IN & (uint8_t)(1 << 1)) && (P1IN & (uint8_t)(1 << 4))))
        {
        }
    }
}