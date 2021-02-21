# Ansible Command

## Ansible vault

- Encrypting Unencrypted Files
```
ansible-vault encrypt foo.yml
```

- Decrypting Encrypted Files
```
ansible-vault decrypt foo.yml
```

## Playbook

Provision
```
ansible-playbook main.yaml
```