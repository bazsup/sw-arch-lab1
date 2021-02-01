terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "=2.45.1"
    }
  }
}

provider "azurerm" {
  
  features {}
}

resource "azurerm_virtual_network" "example" {
  name                = "example-network"
  address_space       = ["10.0.0.0/16"]
  location            = var.location
  resource_group_name = var.resource_group
}

resource "azurerm_subnet" "example" {
  name                 = "example-subnet"
  resource_group_name  = var.resource_group
  virtual_network_name = azurerm_virtual_network.example.name
  address_prefixes     = ["10.0.2.0/24"]
}

resource "azurerm_public_ip" "example" {
  name                = "example-publicIP"
  resource_group_name = var.resource_group
  location            = var.location
  allocation_method   = "Static"
}

resource "azurerm_network_interface" "example" {
  name                = "example-networkinterface"
  resource_group_name = var.resource_group
  location            = var.location

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.example.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.example.id
  }
}

resource "azurerm_linux_virtual_machine" "example" {
  name                            = "example-instance"
  resource_group_name             = var.resource_group
  location                        = var.location
  size                            = "Standard_B1s"
  network_interface_ids = [azurerm_network_interface.example.id]

  admin_username                  = "azureuser"
  admin_password                  = "ThisisPassw0rd"
  disable_password_authentication = false

  admin_ssh_key {
    username   = "azureuser"
    public_key = file("~/.ssh/simple-web-api.pub")
  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-focal"
    sku       = "20_04-lts"
    version   = "latest"
  }

  custom_data = filebase64("bootstrap.sh")
}

output "ips" {
  value = azurerm_linux_virtual_machine.example.*.public_ip_address
}
