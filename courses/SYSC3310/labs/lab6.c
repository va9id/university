#include "msp.h"
#include <stdio.h>
#include <stdlib.h>

static int which_led = 0;

void PORT1_IRQHandler(void)
{

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
        TA0CTL ^= (uint16_t)(1 << 1);
        TA0CTL &= ~(uint16_t)((1 << 2));
    }
}

void TA0_N_IRQHandler(void)
{

    static uint8_t last_three = 0x00;

    // clear interrupt flag
    TA0CTL &= (uint16_t)(~(1 << 0));
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

    // Timer Configuration
    TA0CTL &= ~(uint16_t)((1 << 5) | (1 << 4)); // stop timer
    TA0CTL &= ~(uint16_t)((1 << 0));            // clear interrupt flag
    TA0CCR0 = (uint16_t)(32767);                // 1 second
    TA0CTL |= (uint16_t)(1 << 8);               // select aux clk
    TA0CTL |= (uint16_t)((1 << 1));             // interrupt enable
    TA0CTL |= (uint16_t)((1 << 4));             // up mode

    // NVIC configuration
    NVIC_SetPriority(TA0_N_IRQn, 2); //
    NVIC_ClearPendingIRQ(TA0_N_IRQn);
    NVIC_EnableIRQ(TA0_N_IRQn);

    NVIC_SetPriority(PORT1_IRQn, 3);
    NVIC_ClearPendingIRQ(PORT1_IRQn);
    NVIC_EnableIRQ(PORT1_IRQn);

    // Globally enable interrupts in CPU
    __ASM("CPSIE I");

    while (1)
    {
    }
}