cat > code/setup.sh << 'EOF'
#!/bin/sh
chmod +x /project/code/gradlew
exec /project/code/gradlew "$@"
EOF

chmod +x code/setup.sh
git add code/setup.sh
git commit -m "Add setup script to fix permissions inside container"
git push