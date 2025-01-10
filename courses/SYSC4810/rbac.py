from datetime import datetime, time


class RBAC:
    def __init__(self):
        self.__roles = []
        self.__capabilities = {}

    def add_role(self, role: str) -> None:
        if role not in self.__roles:
            self.__roles.append(role)
            self.__capabilities[role] = set()

    def add_capability(self, role: str, capability: str) -> None:
        if role in self.__roles:
            self.__capabilities[role].add(capability)

    def get_roles(self) -> list:
        return self.__roles

    def list_capabilities(self, role: str) -> None:
        if role in self.__roles:
            # If the roll is a Teller and its outside of working access, deny access
            if role == "T" and not self.is_during_working_hours():
                print(
                    "You have no accesses because it is not during the working hours (09:00 - 17:00)"
                )
            else:
                print("Your capabilites include: ")
                capabilities = self.__capabilities[role]
                for capability in capabilities:
                    print(f"\t{capability}")

    def is_during_working_hours(self) -> bool:
        current_time = datetime.now().time()
        start_time = time(9, 0)
        end_time = time(17, 0)
        if start_time <= current_time <= end_time:
            return True
        else:
            return False
