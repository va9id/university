#include "msp.h"
#include <stdio.h>
#include <stdlib.h>

void PORT1_IRQHandler(void)
{
    static int which_led = 0;
    static uint8_t last_three = 0x00;

    if ((P1IFG & (uint8_t)(1 << 1)) != 0)
    {

        P1IFG &= ~(uint8_t)(1 << 1);
        if (which_led)
        {
            which_led = 0;
        }
        else
        {
            which_led = 1;
        }
    }
    if ((P1IFG & (uint8_t)(1 << 4)) != 0)
    {
        P1IFG &= ~(uint8_t)(1 << 4);
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
}

int main()
{

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

    // Device interrupt configuration
    P1IES |= (uint8_t)0x12;
    P1IFG &= (uint8_t)~0x12;
    P1IE |= (uint8_t)0x12;

    // NVIC configuration
    NVIC_SetPriority(PORT1_IRQn, 2);
    NVIC_ClearPendingIRQ(PORT1_IRQn);
    NVIC_EnableIRQ(PORT1_IRQn);

    // Globally enable interrupts in CPU
    __ASM("CPSIE I");

    while (1)
    {
    }
}