from rbac import RBAC
import password, getpass


ROLES = {
    "C": "Client (C)",
    "PC": "Premium Client (PC)",
    "FA": "Financial Advisor (FA)",
    "FP": "Financial Planner (FP)",
    "IA": "Investment Analyst (IA)",
    "TS": "Tech Support (TS)",
    "T": "Teller (T)",
    "CO": "Compliance Officer (CO)",
}


def add_roles_to_rbac(rbac: RBAC) -> RBAC:
    """
    Adds all the necessary roles to the RBAC.
    """
    rbac.add_role("C")  # Client
    rbac.add_role("PC")  # Premium Client
    rbac.add_role("FA")  # Financial Advisor
    rbac.add_role("FP")  # Financial Planner
    rbac.add_role("IA")  # Investment Analyst
    rbac.add_role("TS")  # Tech Support
    rbac.add_role("T")  # Teller
    rbac.add_role("CO")  # Compliance Officers
    return rbac


def add_role_capabilities(rbac: RBAC) -> RBAC:
    """
    Adds all the capabilities for each role to the RBAC.
    """
    rbac.add_capability("C", "View Account Balance")
    rbac.add_capability("C", "View Investment Portfolio")
    rbac.add_capability("C", "Contact your Financial Adviser")

    rbac.add_capability("PC", "View Account Balance")
    rbac.add_capability("PC", "View Investment Portfolio")
    rbac.add_capability("PC", "Modify Investment Portfolio")
    rbac.add_capability("PC", "Contact your Investment Analyst")
    rbac.add_capability("PC", "Contact your Financial Adviser")
    rbac.add_capability("PC", "Contact your Financial Planner")

    rbac.add_capability("FA", "View a Client's Account Balance")
    rbac.add_capability("FA", "View a Client's Investment Portfolio")
    rbac.add_capability("FA", "Modify a Client's Investment Portfolio")
    rbac.add_capability("FA", "View Private Consumer Instruments")

    rbac.add_capability("FP", "View a Client's Account Balance")
    rbac.add_capability("FP", "View a Client's Investment Portfolio")
    rbac.add_capability("FP", "Modify a Client's Investment Portfolio")
    rbac.add_capability("FP", "View Private Consumer Instruments")
    rbac.add_capability("FP", "View Money Market Instruments")

    rbac.add_capability("IA", "View a Client's Account Balance")
    rbac.add_capability("IA", "View a Client's Investment Portfolio")
    rbac.add_capability("IA", "Modify a Client's Investment Portfolio")
    rbac.add_capability("IA", "View Private Consumer Instrument")
    rbac.add_capability("IA", "View Money Market Instrument")
    rbac.add_capability("IA", "View Interest Instrument")
    rbac.add_capability("IA", "View Derivates Trading")

    rbac.add_capability("TS", "View Client's Information")
    rbac.add_capability("TS", "Request Access to Client's Account")

    rbac.add_capability("T", "View a Client's Account Balance")
    rbac.add_capability("T", "View a Client's Investment Portfolio")
    rbac.add_capability("T", "Modify a Client's Investment Portfolio")

    rbac.add_capability("CO", "View a Client's Account Balance")
    rbac.add_capability("CO", "View a Client's Investment Portfolio")
    rbac.add_capability("CO", "Modify a Client's Investment Portfolio")
    rbac.add_capability("CO", "Validate Investment Portfolio Modifications")

    return rbac


if __name__ == "__main__":
    rbac = RBAC()
    add_roles_to_rbac(rbac)
    add_role_capabilities(rbac)

    print("Finvest Holdings")
    print("Client Holdings and Information System")
    print("-------------------------------------------")

    while True:
        u = input("Enter username: ")
        p = getpass.getpass("Enter password: ")

        if password.check_username_exists(u):
            if password.login(u, p):
                print("ACCESS GRANTED\n")
                record = password.get_user_record(u)
                print(f"Welcome {ROLES[record[3]]} {record[0]}\n")
                rbac.list_capabilities(record[3])
                break
            else:
                print("Invalid password, try again\n")
        else:
            if password.valid_password(u, p, exclusions=password.get_exclusions()):
                print("\nYou are a new user\nFinvest Holdings Roles are: ")
                print(", ".join(str(value) for value in ROLES.values()))
                role = input("Please enter the abbreviation of your role: ")
                if role not in rbac.get_roles():
                    print("Invalid role caused enrollment to fail, try again")
                else:
                    password.write_to_passwd(u, p, role)
                    print("\nACCESS GRANTED\n")
                    print(f"Welcome {ROLES[role]} {u}\n")
                    rbac.list_capabilities(role)
                    break
            else:
                print("Invalid password")
