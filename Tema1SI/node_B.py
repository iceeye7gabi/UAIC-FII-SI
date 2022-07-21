from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad
import socket
from hashlib import md5
from base64 import b64decode
from base64 import b64encode
client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
socket.bind(("127.0.0.2", 23457))
universal_password = 'anaaremeremulte'


class AESComponent:
    def __init__(self, key):
        #https://docs.python.org/3.5/library/hashlib.html
        self.my_key = md5(key.encode('utf8')).digest()

    def encrypt_message(self, input_data):
        my_vector = get_random_bytes(AES.block_size)
        self.my_cipher = AES.new(self.my_key, AES.MODE_CBC, my_vector)
        return b64encode(my_vector + self.my_cipher.encrypt(pad(input_data.encode('utf-8'),
                                                                AES.block_size)))

    def decrypt_message(self, input_data):
        initial_data = b64decode(input_data)
        self.my_cipher = AES.new(self.my_key, AES.MODE_CBC, initial_data[:AES.block_size])
        return unpad(self.my_cipher.decrypt(initial_data[AES.block_size:]), AES.block_size)


def message_node_A(message):
    socket.sendto(bytes(message.encode("utf-8")), addrA)


def receive_node_A():
    dataA, addrA = socket.recvfrom(3867)
    return dataA


def message_key_manager(message):
    client_socket.sendto(message, ("127.0.0.1", 23456))


def receive_key_manager():
    data_key_manager, addr_key_manager = client_socket.recvfrom(3866)
    return data_key_manager


def encryption_CBC():
    message_key_manager(dataA)
    my_data = receive_key_manager()
    output_password = AESComponent(universal_password).decrypt_message(my_data).decode("utf-8")
    print(output_password)
    message_node_A("afirmativ")
    my_data = receive_node_A()
    print(f' A {str(my_data)}')
    my_data = receive_node_A()
    print(f'Inainte de a fi decriptat: {str(my_data)}')
    print(f'Dupa: {AESComponent(output_password).decrypt_message(my_data).decode("utf-8")}')


def encryption_ECB():
    message_key_manager("ECB".encode("utf-8"))
    my_data = receive_key_manager()
    my_decipher = AES.new(universal_password.encode("utf-8"), AES.MODE_ECB)
    message_node_A("ready")
    output_message = receive_node_A()
    print(output_message.decode("utf-8"))
    output_password = my_decipher.decrypt(my_data)
    decrypt_ECB(output_password)


def decrypt_ECB(password):
    dataA, addrA = socket.recvfrom(3867)
    my_str = dataA.decode("utf-8")
    nb = int(my_str)
    my_decipher = AES.new(password, AES.MODE_ECB)
    while nb != 0:
        my_block = receive_node_A()
        print(my_block)
        initial_message.append(my_decipher.decrypt(my_block))
        nb = nb - 1
        print(nb)
    output_message = ""
    for iterator_word in initial_message:
        output_message = output_message + iterator_word.decode("utf+8")
    print(output_message)


if __name__ == '__main__':
    input_node_A = True
    initial_message = []
    while input_node_A:
        dataA, addrA = socket.recvfrom(3867)
        input_node_A = False
    my_str = dataA.decode("utf-8")
    if my_str == "CBC":
        encryption_CBC()
    else:
        encryption_ECB()
