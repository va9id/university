#include "msp.h"
#include <stdio.h>
#include <stdlib.h>

void RGB_Toggle(void);
void TA0_N_IRQHandler(void);
void TA1_N_IRQHandler(void);
void TA1_0_IRQHandler(void);

void TA0_N_IRQHandler(void)
{

    // clear interrupt flag
    TA0CTL &= (uint16_t)(~(1 << 0));
    // toggle red
    P1OUT ^= (uint8_t)(1 << 0); // RED LED
}

void TA1_N_IRQHandler(void)
{
    TA1CTL &= ~(uint16_t)((1 << 0)); // Clear TAIFG
    RGB_Toggle();
}

void TA1_0_IRQHandler(void)
{

    TA1CCTL0 &= ~(uint16_t)((1 << 0)); // clear CCIFG
    RGB_Toggle();
}

void RGB_Toggle(void)
{
    static uint8_t last_three = 0x00;

    if (last_three == 0x00)
    {
        TA1CTL &= (uint16_t)(~((1 << 5) | (1 << 4)));
        TA1CCR0 = (uint16_t)(32767 / 2);           // 0.5 seconds
        TA1CTL |= (uint16_t)((1 << 4) | (1 << 5)); // updown mode
    }
    else if (last_three == 0x08)
    {
        last_three = 0x00;
    }
    last_three += 0x01;
    P2OUT &= ~(uint8_t)(((1 << 2) | (1 << 1) | (1 << 0)));
    P2OUT |= last_three;
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
    P1OUT |= (uint8_t)(1 << 0);
    P2OUT &= ~(uint8_t)(((1 << 2) | (1 << 1) | (1 << 0)));

    // pull up resistor
    P1OUT |= (uint8_t)((1 << 4) | (1 << 1));
    // enabling resistor
    P1REN |= (uint8_t)((1 << 4) | (1 << 1));

    // TimerA0 Configuration
    TA0CTL &= ~(uint16_t)((1 << 5) | (1 << 4)); // stop timer
    TA0CTL &= ~(uint16_t)((1 << 0));            // clear interrupt flag
    TA0CCR0 = (uint16_t)(32767);                // 1 second
    TA0CTL |= (uint16_t)(1 << 8);               // select aux clk
    TA0CTL |= (uint16_t)((1 << 1));             // interrupt enable
    TA0CTL |= (uint16_t)((1 << 4));             // up mode

    // TimerA1 Configuration
    TA1CTL &= ~(uint16_t)((1 << 5) | (1 << 4)); // stop timer
    TA1CTL &= ~(uint16_t)((1 << 0));            // clear interrupt flag
    TA1CCTL0 &= ~(uint16_t)(1 << 0);            // clear intterrupt flag
    TA1CCR0 = (uint16_t)(32767 / 10);           // 0.1 seconds
    TA1CTL |= (uint16_t)(1 << 8);               // select aux clk
    TA1CTL |= (uint16_t)((1 << 1));             // interrupt enable for N
    TA1CCTL0 |= (uint16_t)(1 << 4);             // enable intterupt for 0
    TA1CTL |= (uint16_t)((1 << 4) | (1 << 5));  // updown mode

    // NVIC TA0 configuration
    NVIC_SetPriority(TA0_N_IRQn, 2);
    NVIC_ClearPendingIRQ(TA0_N_IRQn);
    NVIC_EnableIRQ(TA0_N_IRQn);

    // NVIC TA1 config
    NVIC_SetPriority(TA1_N_IRQn, 2);
    NVIC_ClearPendingIRQ(TA1_N_IRQn);
    NVIC_EnableIRQ(TA1_N_IRQn);

    NVIC_SetPriority(TA1_0_IRQn, 2);
    NVIC_ClearPendingIRQ(TA1_0_IRQn);
    NVIC_EnableIRQ(TA1_0_IRQn);

    // Globally enable interrupts in CPU
    __ASM("CPSIE I");

    while (1)
    {
    }
}