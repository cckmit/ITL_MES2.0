#!/bin/bash
mkdir -p /home/cifiadmin/.ssh
chmod 755 /home/cifiadmin/.ssh
cat >> /home/cifiadmin/.ssh/authorized_keys <<EOF
ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDsFDIHDKoLBlrd/GMX5GXdoF6Wg4WcRkAy0BjsaHt1QE9jDcxKrvgeD75r76UXGY7GElWgCHeM6v0+lY3EEnukzRcutshiD9PVjoEM2PunHVAia8sdbm8FP+eVb60vdpBTwzuLa7hXQEtELzJxCwvd01e5Hor7NfgJs62k2lHc5u2HfyOODuPCMzPsHL+bIUInzPNCNFFzW2bw6KB5DtzLpACT26kvUgC5ade5yXWathlXbAE5WeNCydvx8WWCl+jI+kxjdHBe6/s85Fo9lfJwJvcYExEH14wZWS1PyCYclk7UK1N3ORz7SdjD+WdI3YEEAS4kM9KaVxRbffya3QLx gitlab-runner@522e7b401c28
EOF
chown -R cifiadmin.cifiadmin /home/cifiadmin/.ssh
chmod 600 /home/cifiadmin/.ssh/authorized_keys
ls -al /home/cifiadmin/.ssh
